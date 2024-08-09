import type { CategoryListResponse, CategoryRequest } from '@/types';
import { MemberInfo } from '@/types/authentication';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const CATEGORY_API_URL = `${API_URL}/categories`;

export const getCategoryList = async ({ memberId }: Pick<MemberInfo, 'memberId'>) => {
  const url = new URL(CATEGORY_API_URL);

  url.searchParams.append('memberId', String(memberId));

  const response = await customFetch<CategoryListResponse>({
    url: url.toString(),
  });

  if ('categories' in response) {
    return response;
  }

  throw new Error(response.detail);
};

export const postCategory = async (newCategory: CategoryRequest) =>
  await customFetch({
    method: 'POST',
    url: `${CATEGORY_API_URL}`,
    body: JSON.stringify(newCategory),
  });
