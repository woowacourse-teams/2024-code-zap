import { VISIBILITY_OPTIONS } from '@/service/constants';

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
  ordinal: number;
}

export interface Template {
  id: number;
  title: string;
  description: string;
  sourceCodes: SourceCodes[];
  category: Category;
  tags: Tag[];
  likesCount: number;
  isLiked: boolean;
  createdAt: string;
  modifiedAt: string;
  member: {
    id: number;
    name: string;
  };
  visibility: TemplateVisibility;
}

export interface TemplateListItem {
  id: number;
  title: string;
  description: string;
  thumbnail: Thumbnail;
  tags: Tag[];
  likesCount: number;
  isLiked: boolean;
  createdAt: string;
  modifiedAt: string;
  member: {
    id: number;
    name: string;
  };
  visibility: TemplateVisibility;
}

export type TemplateVisibility = keyof typeof VISIBILITY_OPTIONS;
