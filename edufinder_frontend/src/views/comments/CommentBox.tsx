import CardBox from 'src/components/shared/CardBox.tsx';
import { UserComment, UserReply, VoteType } from 'src/types/comment/userComment.ts';
import { useEffect, useState } from 'react';
import AddReplyBox from 'src/views/comments/AddReplyBox.tsx';
import CONSTANTS from 'src/constants.ts';
import toast from 'react-hot-toast';
import { Icon } from '@iconify/react';
import { useAuth } from 'src/context/AuthProvider.tsx';

const CommentBox = ({comment, getComments}: {comment: UserComment; getComments: () => void; }) => {

  const [showReplyBox, setShowReplyBox] = useState(false);
  const [userVoteType, setUserVoteType] = useState<VoteType>(comment.voteSummary.userVoteType);
  const [upvotes, setUpvotes] = useState<number>(comment.voteSummary.upvoteCount);
  const [downvotes, setDownvotes] = useState<number>(comment.voteSummary.downvoteCount);
  const { user, isLoggedIn } = useAuth();

  useEffect(() => {
    if (comment.voteSummary.userVoteType) {
      setUserVoteType(comment.voteSummary.userVoteType);
    }
    console.log(comment.voteSummary)
  }, [comment.voteSummary.userVoteType]);

  const handleReplySubmit = async (content: string) => {
    try {
      const res = await fetch(CONSTANTS.backendEndpoint + '/comments/' + comment.id + '/replies', {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ content: content }),
      });

      if (!res.ok) throw new Error('Failed to post reply');
      toast.success('Reply successfully posted!');
      setShowReplyBox(false);

      getComments();

    } catch (err) {
      toast.error('Failed to post reply');
      console.log(err);
    }
  }

  const handleVote = async (voteType: VoteType) => {

    if (voteType === 'UPVOTE' && userVoteType === 'UPVOTE') voteType = 'NOVOTE';
    if (voteType === 'DOWNVOTE' && userVoteType === 'DOWNVOTE') voteType = 'NOVOTE';

    try {

      const res = await fetch(CONSTANTS.backendEndpoint + '/comments/' + comment.id + '/votes', {
        method: 'PUT',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ voteType: voteType }),
      });

      console.log("Vote: ", res);

      if (!res.ok) throw new Error('Failed to change vote type');

      if (voteType === 'UPVOTE') {
        setUpvotes((prev) => userVoteType === 'UPVOTE' ? prev : prev + 1);
        setDownvotes((prev) => userVoteType === 'DOWNVOTE' ? prev - 1 : prev);
      } else if (voteType === 'DOWNVOTE') {
        setDownvotes((prev) => userVoteType === 'DOWNVOTE' ? prev : prev + 1);
        setUpvotes((prev) => userVoteType === 'UPVOTE' ? prev - 1 : prev);
      } else {
        if (userVoteType === 'UPVOTE') setUpvotes((prev) => prev - 1);
        if (userVoteType === 'DOWNVOTE') setDownvotes((prev) => prev - 1);
      }

      setUserVoteType(voteType);
    } catch (err) {
      console.log(err);
      toast.error('Failed to vote');
    }

  }

  const handleDeleteComment = async () => {
    try {
      const res = await fetch(CONSTANTS.backendEndpoint + '/comments/' + comment.id, {
        method: 'DELETE',
        credentials: 'include',
      });

      if (!res.ok) throw new Error('Failed to delete comment');

      toast.success('Comment deleted!');
      getComments();
    } catch (err) {
      toast.error('Failed to delete comment');
      console.error(err);
    }
  };

  return (
    <div className="mt-2 mb-2">
      <CardBox key={comment.id} className="mt-2 mb-2">
        <div className="flex gap-4 items-center">
          <div className="w-full md:pe-0 pe-10">
            <p className="text-ld text-xs font-thin text-gray-400">Posted by {comment.username} at {comment.createdAt.toLocaleString()}</p>
            <div className="flex justify-between items-center">
              <p className="text-ld text-sm font-medium">{comment.content}</p>
              {isLoggedIn && user?.username === comment.username && (
                <button
                  onClick={handleDeleteComment}
                  className="text-red-500 hover:text-red-700"
                  title="Delete Comment"
                >
                  <Icon icon="mdi:trash-can-outline" width={18} />
                </button>
              )}
            </div>

            <div className="flex flex-row items-center px-2">
              <button
                onClick={() => handleVote('UPVOTE')}
                className={`hover:text-blue-600 ${userVoteType === 'UPVOTE' ? 'text-blue-600' : 'text-gray-500'}`}
              >
                <Icon icon="mdi:arrow-up-bold" width={18} />
              </button>
              <span className="text-sm">{upvotes} - {downvotes}</span>
              <button
                onClick={() => handleVote('DOWNVOTE')}
                className={`hover:text-red-600 ${userVoteType === 'DOWNVOTE' ? 'text-red-600' : 'text-gray-500'}`}
              >
                <Icon icon="mdi:arrow-down-bold" width={18} />
              </button>
            </div>
          </div>
        </div>
        )
      </CardBox>
      {
        comment.replies.map((reply: UserReply) => (
          <CardBox key={reply.id} className="mt-2 mb-2 ml-24 w-[calc(100%-6rem)]">
            <p className="text-ld text-xs font-thin text-gray-400">Posted by {reply.username} at {reply.createdAt.toLocaleString()}</p>
            <p className="text-ld text-sm font-medium">{reply.content}</p>
          </CardBox>
        ))
      }
      {
        !showReplyBox && (
          <div className={`${comment.replies.length ? "ml-24" : "ml-0"} mt-2`}>
            <button
              onClick={() => setShowReplyBox(true)}
              className="text-blue-600 text-sm underline hover:opacity-80"
            >
              Reply in thread
            </button>
          </div>
        )
      }
      {
        showReplyBox && (
          <AddReplyBox onSubmit={handleReplySubmit} setShowReplyBox={setShowReplyBox} commentReplyLength={comment.replies.length} />
        )
      }
    </div>
  )
}

export default CommentBox;