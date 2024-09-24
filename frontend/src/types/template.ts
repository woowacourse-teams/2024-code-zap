export interface SourceCodes {
  id?: number;
  filename: string;
  content: string;
  ordinal: number;
}

export interface Thumbnail {
  filename: string;
  content: string;
}

export interface Tag {
  id: number;
  name: string;
}

export interface Category {
  id: number;
  name: string;
}

export interface Template {
  id: number;
  title: string;
  description: string;
  sourceCodes: SourceCodes[];
  category: Category;
  tags: Tag[];
  likeCount: number;
  isLiked: boolean;
  createdAt: string;
  modifiedAt: string;
  member: {
    id: number;
    name: string;
  };
}

export interface TemplateListItem {
  id: number;
  title: string;
  description: string;
  thumbnail: Thumbnail;
  tags: Tag[];
  likeCount: number;
  isLiked: boolean;
  createdAt: string;
  modifiedAt: string;
  member: {
    id: number;
    name: string;
  };
}
