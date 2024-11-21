import { HttpResponse } from 'msw';

import { END_POINTS } from '@/routes';
import type { CategoryUploadRequest, CategoryEditRequest, CategoryDeleteRequest, CustomError } from '@/types';

import { apiClient } from './config';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL || 'https://default-url.com';

export const CATEGORY_API_URL = `${API_URL}${END_POINTS.CATEGORIES}`;

export const getCategoryList = async (memberId: number) => {
  const queryParams = new URLSearchParams({
    memberId: memberId.toString(),
  });
  const response = await apiClient.get(`${END_POINTS.CATEGORIES}?${queryParams.toString()}`);

  return await response.json();
};

export const postCategory = async (newCategory: CategoryUploadRequest) => {
  const response = await apiClient.post(`${END_POINTS.CATEGORIES}`, newCategory);

  return await response.json();
};

export const editCategory = async ({ id, name }: CategoryEditRequest) =>
  await apiClient.put(`${END_POINTS.CATEGORIES}/${id}`, { name });

export const deleteCategory = async ({ id }: CategoryDeleteRequest) => {
  const response = await customFetch<HttpResponse>({
    method: 'DELETE',
    url: `${CATEGORY_API_URL}/${id}`,
  });

  if (typeof response === 'object' && response !== null && 'status' in response) {
    throw response as CustomError;
  }

  return response;
};
