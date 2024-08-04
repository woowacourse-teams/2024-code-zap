export interface Snippet {
  id?: number;
  filename: string;
  content: string;
  ordinal: number;
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
  templates: Template[];
}

export interface TemplateUploadRequest {
  title: string;
  snippets: Snippet[];
}

export interface TemplateEditRequest {
  title: string;
  createSnippets: Snippet[];
  updateSnippets: Snippet[];
  deleteSnippetIds: number[];
}
