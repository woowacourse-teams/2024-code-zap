import { Category, SourceCodes, Tag, TemplateListItem } from './template';

export interface TemplateListResponse {
  templates: TemplateListItem[];
  totalPages: number;
  totalElements: number;
}

export interface TemplateUploadRequest {
  title: string;
  description: string;
  sourceCodes: SourceCodes[];
  thumbnailOrdinal: number;
  categoryId: number;
  tags: string[];
}

export interface TemplateEditRequest {
  title: string;
  description: string;
  createSourceCodes: SourceCodes[];
  updateSourceCodes: SourceCodes[];
  deleteSourceCodeIds: number[];
  categoryId: number;
  tags: string[];
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
  pageSize?: number;
  memberId: number | undefined;
}

export type SortingKey = 'modifiedAt,asc' | 'modifiedAt,desc';

export type SortingOption = { key: SortingKey; value: string };
