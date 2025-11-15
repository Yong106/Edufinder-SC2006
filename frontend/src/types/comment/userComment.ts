export type VoteType = 'UPVOTE' | 'DOWNVOTE' | 'NOVOTE';

export interface VoteSummary {
  userVoteType: VoteType;
  upvoteCount: number;
  downvoteCount: number;
}

export interface UserReply {
  id: number;
  username: string;
  content: string;
  createdAt: string;
}

export interface UserComment {
  id: number;
  username: string;
  content: string;
  createdAt: string;
  replies: UserReply[];
  voteSummary: VoteSummary;
}

export interface CommentsResponse {
  comments: UserComment[];
}