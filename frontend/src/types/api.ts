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
  categoryId?: number;
  tagIds?: number[];
  page?: number;
  pageSize?: number;
  keyword?: string;
  memberId: number | undefined;
}
