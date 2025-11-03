import { useEffect, useState } from 'react';
import CONSTANTS from 'src/constants.ts';
import CommentBox from 'src/views/comments/CommentBox.tsx';
import { UserComment } from 'src/types/comment/userComment.ts';

const CommentSection = ({schoolId}: {schoolId: string}) => {

  const [comments, setComments] = useState<UserComment[]>([]);
  const [isCommentsLoading, setIsCommentsLoading] = useState<boolean>(true);

  useEffect(() => {
    const getComments = async () => {
      console.log("Fetching comments...");
      const res = await fetch(CONSTANTS.backendEndpoint + '/schools/' + schoolId + '/comments', {
        method: 'GET',
      });

      const commentData = await res.json();
      setComments(commentData.comments);
      console.log("Comments:", comments);
      setIsCommentsLoading(false);
    }

    getComments();
  }, []);

  return (
    <div className="mt-5 mb-2">
      <h2 className="text-xl">Comments</h2>
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