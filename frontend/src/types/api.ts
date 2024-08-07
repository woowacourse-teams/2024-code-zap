import { Category, Snippet, Template } from './template';

export interface TemplateListResponse {
  templates: Template[];
  totalPages: number;
  totalElements: number;
  numberOfElements: number;
}

export interface TemplateUploadRequest {
  title: string;
  description: string;
  snippets: Snippet[];
  categoryId: number;
  tags: string[];
}

export interface TemplateEditRequest {
  title: string;
  description: string;
  createSnippets: Snippet[];
  updateSnippets: Snippet[];
  deleteSnippetIds: number[];
  categoryId: number;
  tags: string[];
}

export interface CategoryListResponse {
  categories: Category[];
}

export interface CategoryRequest {
  name: string;
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

export type SortingKey = 'createdAt,asc' | 'createdAt,desc';

export type SortingOption = { key: SortingKey; value: string };
