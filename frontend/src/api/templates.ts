import { END_POINTS } from '@/routes';
import type {
  Template,
  TemplateRequest,
  TemplateEditRequest,
  TemplateListResponse,
  TemplateUploadRequest,
  TemplateListRequest,
} from '@/types';
import { SortingOption } from '@/types';

import { apiClient } from './ApiClient';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL || 'https://default-url.com';

export const TEMPLATE_API_URL = `${API_URL}${END_POINTS.TEMPLATES_EXPLORE}`;

export const PAGE_SIZE = 20;

export const SORTING_OPTIONS: SortingOption[] = [
  {
    key: 'modifiedAt,desc',
    value: '최근 순',
  },
  {
    key: 'modifiedAt,asc',
    value: '오래된 순',
  },
  {
    key: 'likesCount,desc',
    value: '좋아요 순',
  },
];

export const DEFAULT_SORTING_OPTION = SORTING_OPTIONS[0];

export const getTemplateList = async ({
  keyword,
  categoryId,
  tagIds,
  sort = DEFAULT_SORTING_OPTION.key,
  page = 1,
  size = PAGE_SIZE,
  memberId,
}: TemplateListRequest) => {
  const queryParams = new URLSearchParams({
    sort,
    page: page.toString(),
    size: size.toString(),
  });

  if (memberId) {
    queryParams.append('memberId', memberId.toString());
  }

  if (categoryId) {
    queryParams.append('categoryId', categoryId.toString());
  }

  if (tagIds?.length !== 0 && tagIds !== undefined) {
    queryParams.append('tagIds', tagIds.toString());
  }

  if (keyword) {
    queryParams.append('keyword', keyword);
  }

  const url = `${TEMPLATE_API_URL}?${queryParams.toString()}`;

  const response = await customFetch<TemplateListResponse>({
    url,
  });

  if ('templates' in response) {
    return response;
  }

  throw new Error(response.detail);
};

export const getLikedTemplateList = async ({
  page = 1,
  size = PAGE_SIZE,
  sort = DEFAULT_SORTING_OPTION.key,
}: TemplateListRequest) => {
  const queryParams = new URLSearchParams({
    sort,
    page: page.toString(),
    size: size.toString(),
  });

  const response = await apiClient.get(`${END_POINTS.LIKED_TEMPLATES}?${queryParams.toString()}`);
  const data = response.json();

  return data;
};

export const getTemplateExplore = async ({
  sort = DEFAULT_SORTING_OPTION.key,
  page = 1,
  size = PAGE_SIZE,
  keyword,
  tagIds,
}: TemplateListRequest): Promise<TemplateListResponse> => {
  const queryParams = new URLSearchParams({
    sort,
    page: page.toString(),
    size: size.toString(),
  });

  if (keyword) {
    queryParams.append('keyword', keyword);
  }

  if (tagIds?.length !== 0 && tagIds !== undefined) {
    queryParams.append('tagIds', tagIds.toString());
  }

  const response = await apiClient.get(`${END_POINTS.TEMPLATES_EXPLORE}?${queryParams.toString()}`);
  const data = response.json();

  return data;
};

export const getTemplate = async ({ id }: TemplateRequest): Promise<Template> => {
  const response = await apiClient.get(`${END_POINTS.TEMPLATES_EXPLORE}/${id}`);
  const data = response.json();

  return data;
};

export const postTemplate = async (newTemplate: TemplateUploadRequest): Promise<Response> =>
  await apiClient.post(`${END_POINTS.TEMPLATES_EXPLORE}`, newTemplate);

export const editTemplate = async ({ id, template }: { id: number; template: TemplateEditRequest }): Promise<void> => {
  await customFetch({
    method: 'POST',
    url: `${TEMPLATE_API_URL}/${id}`,
    body: JSON.stringify(template),
  });
};

export const deleteTemplate = async (idList: number[]): Promise<void> => {
  await customFetch({
    method: 'DELETE',
    url: `${TEMPLATE_API_URL}/${idList.join(',')}`,
  });
};
