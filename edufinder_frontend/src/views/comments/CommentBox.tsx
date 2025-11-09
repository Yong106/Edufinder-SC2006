import CardBox from 'src/components/shared/CardBox.tsx';
import { UserComment, UserReply } from 'src/types/comment/userComment.ts';
import { useState } from 'react';
import AddReplyBox from 'src/views/comments/AddReplyBox.tsx';
import CONSTANTS from 'src/constants.ts';
import toast from 'react-hot-toast';

const CommentBox = ({comment, getComments}: {comment: UserComment; getComments: () => void; }) => {

  const [showReplyBox, setShowReplyBox] = useState(false);

  const handleReplySubmit = async (content: string) => {
    try {
      const res = await fetch(CONSTANTS.backendEndpoint + '/comments/' + comment.id + '/replies', {
        method: 'POST',
        credentials: 'include',
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

  return (
    <div className="mt-2 mb-2">
      <CardBox key={comment.id} className="mt-2 mb-2">
        <div className="flex gap-4 items-center">
          <div className="w-full md:pe-0 pe-10">
            <p className="text-ld text-xs font-thin text-gray-400">Posted by {comment.username} at {comment.createdAt.toLocaleString()}</p>
            <p className="text-ld text-sm font-medium">{comment.content}</p>
          </div>
        </div>
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