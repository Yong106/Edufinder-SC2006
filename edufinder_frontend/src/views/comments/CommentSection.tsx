import { useEffect, useState } from 'react';
import CONSTANTS from 'src/constants.ts';
import CommentBox from 'src/views/comments/CommentBox.tsx';
import { UserComment } from 'src/types/comment/userComment.ts';
import AddCommentBox from 'src/views/comments/AddCommentBox.tsx';
import toast from 'react-hot-toast';

const CommentSection = ({schoolId}: {schoolId: string}) => {

  const [comments, setComments] = useState<UserComment[]>([]);
  const [isCommentsLoading, setIsCommentsLoading] = useState<boolean>(true);

  const getComments = async () => {
    setIsCommentsLoading(true);
    console.log("Fetching comments...");
    const res = await fetch(CONSTANTS.backendEndpoint + '/schools/' + schoolId + '/comments', {
      method: 'GET',
    });

    const commentData = await res.json();
    setComments(commentData.comments);
    console.log("Comments:", comments);
    setIsCommentsLoading(false);
  }

  useEffect(() => {
    getComments();
  }, []);

  const postNewComment = async(content: string) => {
    const res = await fetch(CONSTANTS.backendEndpoint + '/schools/' + schoolId + '/comments', {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ content }),
    });

    if (!res.ok) toast.error("Failed to post comment");
    else toast.success("Comment successfully created!");

    getComments();
  }

  return (
    <div className="mt-5 mb-2">
      <h2 className="text-xl">Comments</h2>
      <AddCommentBox onSubmit={postNewComment} />
      {
        isCommentsLoading ? (
          <div>Loading comments...</div>
        ) : (
          [...comments].reverse().map((comment: UserComment) => (
            <CommentBox comment={comment} />
          ))
        )
      }
    </div>
  )
}

export default CommentSection;