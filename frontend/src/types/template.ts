export interface Snippet {
  id?: number;
  filename: string;
  content: string;
  ordinal: number;
}

export interface Template {
  id: number;
  title: string;
  snippets: Snippet[];
  modifiedAt: string;
}

export interface TemplateListItem {
  id: number;
  title: string;
  thumbnailSnippet: {
    filename: string;
    contentSummary: string;
  };
  modifiedAt: string;
}

export interface TemplateListResponse {
  templates: TemplateListItem[];
}

export interface TemplateUploadRequest {
  title: string;
  snippets: Snippet[];
}
