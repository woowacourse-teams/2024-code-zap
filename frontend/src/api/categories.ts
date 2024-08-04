import type { CategoryListResponse } from '@/types';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const CATEGORY = {
  UNASSIGNED_ID: 1,
};

export const CATEGORY_API_URL = `${API_URL}/categories`;

export const getCategoryList = async (): Promise<CategoryListResponse> =>
  await customFetch({
    url: `${CATEGORY_API_URL}`,
  });
