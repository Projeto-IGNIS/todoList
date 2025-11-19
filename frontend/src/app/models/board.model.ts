export interface Board {
  id?: number;
  title: string;
  favorite: boolean;
  ownerId?: number;
}

export interface CreateBoardRequest {
  title: string;
  ownerId: number;
}
