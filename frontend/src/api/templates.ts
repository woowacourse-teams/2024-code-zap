import { apiClient } from '@/api/config';
import { END_POINTS } from '@/routes';
import type { TemplateRequest, TemplateEditRequest, TemplateUploadRequest, TemplateListRequest } from '@/types';
import { SortingOption } from '@/types';

export const PAGE_SIZE = 20;

export const SORTING_OPTIONS: SortingOption[] = [
  {
    key: 'createdAt,desc',
    value: '최근 생성 순',
  },
  {
    key: 'modifiedAt,desc',
    value: '최근 수정 순',
  },
  {
    key: 'createdAt,asc',
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

  const response = await apiClient.get(`${END_POINTS.TEMPLATES_EXPLORE}?${queryParams.toString()}`);

  return await response.json();
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

  return await response.json();
};

export const getTemplateExplore = async ({
  sort = DEFAULT_SORTING_OPTION.key,
  page = 1,
  size = PAGE_SIZE,
  keyword,
  tagIds,
}: TemplateListRequest) => {
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

  return await response.json();
};

export const getTemplate = async ({ id }: TemplateRequest) => {
  const response = await apiClient.get(`${END_POINTS.TEMPLATES_EXPLORE}/${id}`);

  return await response.json();
};

export const postTemplate = async (newTemplate: TemplateUploadRequest) =>
  await apiClient.post(`${END_POINTS.TEMPLATES_EXPLORE}`, newTemplate);

export const editTemplate = async (template: TemplateEditRequest) =>
  await apiClient.post(`${END_POINTS.TEMPLATES_EXPLORE}/${template.id}`, template);

export const deleteTemplate = async (idList: number[]) =>
  await apiClient.delete(`${END_POINTS.TEMPLATES_EXPLORE}/${idList.join(',')}`);
