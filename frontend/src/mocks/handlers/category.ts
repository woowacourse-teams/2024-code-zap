import { http } from 'msw';

import { API_URL } from '@/api';
import mockCategoryList from '@/mocks/fixtures/categoryList.json';
import { END_POINTS } from '@/routes';
import { Category } from '@/types';
import { mockResponse } from '@/utils/mockResponse';

export const categoryHandlers = [
  http.get(`${API_URL}${END_POINTS.CATEGORIES}`, () => mockResponse({ status: 200, body: { mockCategoryList } })),

  http.post(`${API_URL}${END_POINTS.CATEGORIES}`, async (req) => {
    const newCategory = await req.request.json();

    if (typeof newCategory === 'object' && newCategory !== null) {
      const newId = mockCategoryList.categories.length + 1;
      const category = { id: newId, ...newCategory } as Category;

      mockCategoryList.categories.push(category);

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
    const categoryIndex = mockCategoryList.categories.findIndex((cat) => cat.id.toString() === id);

    if (categoryIndex !== -1 && typeof updatedCategory === 'object' && updatedCategory !== null) {
      mockCategoryList.categories[categoryIndex] = { id: parseInt(id as string), ...updatedCategory } as Category;

      return mockResponse({
        status: 200,
        body: {
          category: mockCategoryList.categories[categoryIndex],
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
    const categoryIndex = mockCategoryList.categories.findIndex((cat) => cat.id.toString() === id);

    if (categoryIndex !== -1) {
      mockCategoryList.categories.splice(categoryIndex, 1);

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
