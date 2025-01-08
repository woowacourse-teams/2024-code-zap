import { apiClient } from '@/api/config';
import { END_POINTS } from '@/routes';
import type { CategoryEditRequest, Category } from '@/types';

export const getCategoryList = async (memberId: number) => {
  const queryParams = new URLSearchParams({
    memberId: memberId.toString(),
  });
  const response = await apiClient.get(`${END_POINTS.CATEGORIES}?${queryParams.toString()}`);

  return await response.json();
};

export const postCategory = async (newCategory: Omit<Category, 'id'>) => {
  const response = await apiClient.post(`${END_POINTS.CATEGORIES}`, newCategory);

  return await response.json();
};

export const editCategory = async (body: CategoryEditRequest) => await apiClient.put(`${END_POINTS.CATEGORIES}`, body);
