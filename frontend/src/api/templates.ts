import { Template, TemplateEditRequest, TemplateListResponse, TemplateUploadRequest } from '@/types/template';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const TEMPLATE_API_URL = `${API_URL}/templates`;

export const getTemplateList = async (): Promise<TemplateListResponse> =>
  await customFetch({
    url: `${TEMPLATE_API_URL}`,
  });

export const getTemplate = async (id: number): Promise<Template> =>
  await customFetch({
    url: `${TEMPLATE_API_URL}/${id}`,
  });

export const postTemplate = async (newTemplate: TemplateUploadRequest) =>
  await customFetch({
    method: 'POST',
    url: `${TEMPLATE_API_URL}`,
    body: JSON.stringify(newTemplate),
  });

export const editTemplate = async ({ id, template }: { id: number; template: TemplateEditRequest }): Promise<void> => {
  await customFetch({
    method: 'POST',
    url: `${TEMPLATE_API_URL}/${id}`,
    body: JSON.stringify(template),
  });
};

export const deleteTemplate = async (id: number): Promise<void> => {
  await customFetch({
    method: 'DELETE',
    url: `${TEMPLATE_API_URL}/${id}`,
  });
};
