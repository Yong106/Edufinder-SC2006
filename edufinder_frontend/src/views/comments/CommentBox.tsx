import CardBox from 'src/components/shared/CardBox.tsx';
import { UserComment, UserReply } from 'src/types/comment/userComment.ts';

const CommentBox = ({comment}: {comment: UserComment}) => {

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
    </div>
  )
}

export default CommentBox;