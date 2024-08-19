import { HttpResponse } from 'msw';

import type {
  CategoryListResponse,
  CategoryUploadRequest,
  CategoryEditRequest,
  CategoryDeleteRequest,
  CustomError,
} from '@/types';
import { MemberInfo } from '@/types';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const CATEGORY_API_URL = `${API_URL}/categories`;

export const getCategoryList = async ({ memberId }: Pick<MemberInfo, 'memberId'>) => {
  const url = `${CATEGORY_API_URL}?memberId=${memberId}`;

  const response = await customFetch<CategoryListResponse>({
    url,
  });

  if ('categories' in response) {
    return response;
  }

  throw new Error(response.detail);
};

export const postCategory = async (newCategory: CategoryUploadRequest) =>
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

  if (response.status >= 400) {
    throw response as CustomError;
  }

  return response;
};
