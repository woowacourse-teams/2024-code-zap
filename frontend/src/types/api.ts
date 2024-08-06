import { Category, Snippet, Template } from './template';

export interface TemplateEditRequest {
  title: string;
  createSnippets: Snippet[];
  updateSnippets: Snippet[];
  deleteSnippetIds: number[];
}

export interface TemplateListResponse {
  templates: Template[];
  totalPages: number;
}

export interface TemplateUploadRequest {
  title: string;
  snippets: Snippet[];
}

export interface CategoryListResponse {
  categories: Category[];
}
