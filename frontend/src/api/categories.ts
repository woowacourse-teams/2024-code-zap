import { HttpResponse } from 'msw';

import { customFetch } from '@/api';
import { END_POINTS } from '@/routes';
import type {
  CategoryListResponse,
  CategoryUploadRequest,
  CategoryEditRequest,
  CategoryDeleteRequest,
  CustomError,
  Category,
} from '@/types';

const API_URL = process.env.REACT_APP_API_URL || 'https://default-url.com';

export const CATEGORY_API_URL = `${API_URL}${END_POINTS.CATEGORIES}`;

export const getCategoryList = async (memberId: number) => {
  const url = `${CATEGORY_API_URL}?memberId=${memberId}`;

  const response = await customFetch<CategoryListResponse>({
    url,
  });

  if ('categories' in response) {
    return response;
  }

  throw new Error(response.detail);
};

export const postCategory = async (newCategory: CategoryUploadRequest): Promise<Category | CustomError> =>
  await customFetch({
    method: 'POST',
    url: `${CATEGORY_API_URL}`,
    body: JSON.stringify(newCategory),
  });

export const editCategory = async ({ id, name }: CategoryEditRequest) =>
  await customFetch({
    method: 'PUT',
    url: `${CATEGORY_API_URL}/${id}`,
    body: JSON.stringify({ name }),
  });

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
