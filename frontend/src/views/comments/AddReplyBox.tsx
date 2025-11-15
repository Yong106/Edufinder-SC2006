import CardBox from 'src/components/shared/CardBox.tsx';
import { FormEvent, useState } from 'react';

interface AddReplyBoxProps {
  onSubmit: (content: string) => void;
  setShowReplyBox: (show: boolean) => void;
  commentReplyLength: number;
}

const AddReplyBox = ({ onSubmit, setShowReplyBox, commentReplyLength }: AddReplyBoxProps) => {

  const [content, setContent] = useState('');

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    if (!content.trim()) return;
    onSubmit(content.trim());
    setContent('');
  };

  return (
    <div className="mt-2 mb-2">
      <CardBox className={`mt-2 mb-2 ${
        commentReplyLength ? 'ml-24 w-[calc(100%-6rem)]' : 'ml-0 w-full'
      }`}>
        <div className="flex gap-4 items-center">
          <div className="w-full md:pe-0 pe-10">
            <form onSubmit={handleSubmit}>
              <label htmlFor="comment" className="block text-sm font-medium text-gray-700 mb-2">
                Add a Reply
              </label>
              <textarea
                id="comment"
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="Write your reply here..."
                className="w-full border rounded-md p-2 text-sm focus:outline-none focus:ring focus:ring-blue-200 resize-none"
                rows={4}
              />
              <div className="flex justify-end mt-2">
                <button
                  type="button"
                  className="bg-gray-300 hover:bg-blue-700 text-gray-800 text-sm px-4 py-2 rounded-md mr-2"
                  onClick={() => {
                    setContent('');
                    setShowReplyBox(false);
                  }}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="bg-blue-600 hover:bg-blue-700 text-white text-sm px-4 py-2 rounded-md"
                >
                  Post Reply
                </button>
              </div>
            </form>
          </div>
        </div>
      </CardBox>
    </div>
  )
}

export default AddReplyBox;