import { Category } from './category';
import { Tag } from './tag';

export interface Snippet {
  id?: number;
  filename: string;
  content: string;
  ordinal: number;
}

export interface Template {
  id: number;
  title: string;
  description: string;
  snippets: Snippet[];
  category: Category;
  tags: Tag[];
  modifiedAt: string;
}

export interface TemplateListItem {
  id: number;
  title: string;
  thumbnailSnippet: {
    filename: string;
    thumbnailContent: string;
  };
  modifiedAt: string;
}

export interface TemplateListResponse {
  templates: TemplateListItem[];
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
