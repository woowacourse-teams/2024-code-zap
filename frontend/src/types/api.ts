import { SORTING_OPTIONS } from '@/models/templates';
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

export interface CategoryEditRequest {
  createCategories: Omit<Category, 'id'>[];
  updateCategories: Category[];
  deleteCategoryIds: number[];
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

export type SortingKey = (typeof SORTING_OPTIONS)[number]['key'];

export type SortingOption = { key: SortingKey; value: string };
