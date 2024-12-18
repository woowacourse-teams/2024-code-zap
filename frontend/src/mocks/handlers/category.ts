import { http } from 'msw';

import { API_URL } from '@/api';
import  categories from '@/mocks/fixtures/categoryList.json';
import { END_POINTS } from '@/routes';
import { Category } from '@/types';
import { mockResponse } from '@/utils/mockResponse';

const mockCategoryList = [...categories.categories];

export const categoryHandlers = [
  http.get(`${API_URL}${END_POINTS.CATEGORIES}`, () =>
    mockResponse({ status: 200, body: { categories: mockCategoryList } }),
  ),

  http.post(`${API_URL}${END_POINTS.CATEGORIES}`, async (req) => {
    const newCategory = await req.request.json();

    if (typeof newCategory === 'object' && newCategory !== null) {
      const newId = mockCategoryList.length + 1;
      const category = { id: newId, ...newCategory } as Category;

      mockCategoryList.push(category);

      return mockResponse({ status: 201, body: { category } });
    }

    return mockResponse({
      status: 400,
      body: {
        message: 'Invalid category data',
      },
    });
  }),

  http.put(`${API_URL}${END_POINTS.CATEGORIES}/:id`, async (req) => {
    const { id } = req.params;
    const updatedCategory = await req.request.json();
    const categoryIndex = mockCategoryList.findIndex((cat) => cat.id.toString() === id);

    if (categoryIndex !== -1 && typeof updatedCategory === 'object' && updatedCategory !== null) {
      mockCategoryList[categoryIndex] = { id: parseInt(id as string), ...updatedCategory } as Category;

      return mockResponse({
        status: 200,
        body: {
          category: mockCategoryList[categoryIndex],
        },
      });
    }

    return mockResponse({
      status: 404,
      body: {
        message: 'Category not found or invalid data',
      },
    });
  }),

  http.delete(`${API_URL}${END_POINTS.CATEGORIES}/:id`, (req) => {
    const { id } = req.params;
    const categoryIndex = mockCategoryList.findIndex((cat) => cat.id.toString() === id);

    if (categoryIndex !== -1) {
      mockCategoryList.splice(categoryIndex, 1);

      return mockResponse({
        status: 204,
      });
    }

    return mockResponse({
      status: 404,
      body: {
        message: 'Category not found',
      },
    });
  }),
];
