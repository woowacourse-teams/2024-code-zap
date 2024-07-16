export interface Snippet {
  id: number;
  filename: string;
  content: string;
  ordinal: number;
}

export interface Template {
  id: number;
  title: string;
  member: {
    id: number;
    nickname: string;
  };
  representative_snippet_ordinal: number;
  snippets: Snippet[];
  modified_at: string;
}

export interface TemplateListItem {
  id: number;
  title: string;
  member: {
    id: number;
    nickname: string;
  };
  representative_snippet: {
    filename: string;
    content_summary: string;
  };
  modified_at: string;
}

export interface TemplateListResponse {
  templates: TemplateListItem[];
}