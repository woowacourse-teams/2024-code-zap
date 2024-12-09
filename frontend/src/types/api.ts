import { Category, SourceCodes, Tag, TemplateListItem, TemplateVisibility } from '@/types';

export interface TemplateListResponse {
  templates: TemplateListItem[];
  paginationSizes: number;
}

export interface TemplateUploadRequest {
  title: string;
  description: string;
  sourceCodes: SourceCodes[];
  thumbnailOrdinal: number;
  categoryId: number;
  tags: string[];
  visibility: TemplateVisibility;
}

export interface TemplateEditRequest {
  id: number;
  title: string;
  description: string;
  createSourceCodes: SourceCodes[];
  updateSourceCodes: SourceCodes[];
  deleteSourceCodeIds: number[];
  categoryId: number;
  tags: string[];
  visibility: TemplateVisibility;
}

export interface LikePostRequest {
  templateId: number;
}

export interface LikeDeleteRequest {
  templateId: number;
}

export interface CategoryListResponse {
  categories: Category[];
}

export interface CategoryUploadRequest {
  name: string;
}

export interface CategoryEditRequest {
  id: number;
  name: string;
}

export interface CategoryDeleteRequest {
  id: number;
}

export interface TagListResponse {
  tags: Tag[];
}

export interface TemplateListRequest {
  keyword?: string;
  categoryId?: number;
  tagIds?: number[];
  sort?: SortingKey;
  page?: number;
  size?: number;
  memberId?: number | undefined;
}

export interface TemplateRequest {
  id: number;
  memberId?: number | undefined;
}

export interface GetMemberNameResponse {
  name: string;
}

export type SortingKey = 'modifiedAt,asc' | 'modifiedAt,desc' | 'likesCount,desc';

export type SortingOption = { key: SortingKey; value: string };
