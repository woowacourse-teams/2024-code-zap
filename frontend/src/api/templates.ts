import { Template, TemplateListResponse, TemplateUploadRequest } from '@/types/template';
import { API_URL, END_POINT } from './config';
import { customFetch } from './customFetch';

export const getTemplateList = async (): Promise<TemplateListResponse> =>
  await customFetch({
    url: `${API_URL}${END_POINT.TEMPLATES}`,
  });

export const getTemplate = async (id: number): Promise<Template> =>
  await customFetch({
    url: `${API_URL}${END_POINT.TEMPLATES}/${id}`,
  });

export const postTemplate = async (newTemplate: TemplateUploadRequest): Promise<void> => {
  await customFetch({
    method: 'POST',
    url: `${API_URL}${END_POINT.TEMPLATES}`,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(newTemplate),
  });
};
