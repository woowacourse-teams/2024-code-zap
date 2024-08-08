import type { CategoryListResponse, CategoryRequest } from '@/types';
import { MemberInfo } from '@/types/authentication';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const CATEGORY_API_URL = `${API_URL}/categories`;

export const getCategoryList = async ({ memberId }: Pick<MemberInfo, 'memberId'>): Promise<CategoryListResponse> => {
  const url = new URL(CATEGORY_API_URL);

  url.searchParams.append('memberId', String(memberId));

  return await customFetch({
    url: url.toString(),
  });
};

export const postCategory = async (newCategory: CategoryRequest) =>
  await customFetch({
    method: 'POST',
    url: `${CATEGORY_API_URL}`,
    body: JSON.stringify(newCategory),
  });
