import { CategoryList, CategoryRequest } from '@/types/category';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const CATEGORY_API_URL = `${API_URL}/categories`;

export const getCategoryList = async (): Promise<CategoryList> =>
  await customFetch({
    url: `${CATEGORY_API_URL}`,
  });

export const postCategory = async (newCategory: CategoryRequest) =>
  await customFetch({
    method: 'POST',
    url: `${CATEGORY_API_URL}`,
    body: JSON.stringify(newCategory),
  });
