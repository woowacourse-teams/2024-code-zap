import { Template, TemplateListResponse, TemplateUploadRequest } from '@/types/template';
import { customFetch } from './customFetch';

const BASE_URL = process.env.REACT_APP_API_URL;

export const TEMPLATE_API_URL = `${BASE_URL}/templates`;

export const getTemplateList = async (): Promise<TemplateListResponse> =>
  await customFetch({
    url: `${TEMPLATE_API_URL}`,
  });

export const getTemplate = async (id: number): Promise<Template> =>
  await customFetch({
    url: `${TEMPLATE_API_URL}/${id}`,
  });

export const postTemplate = async (newTemplate: TemplateUploadRequest): Promise<void> => {
  await customFetch({
    method: 'POST',
    url: `${TEMPLATE_API_URL}`,
    body: JSON.stringify(newTemplate),
  });
};
