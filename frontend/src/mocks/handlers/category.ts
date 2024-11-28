import { HttpResponse, http } from 'msw';

import { API_URL } from '@/api';
import mockCategoryList from '@/mocks/fixtures/categoryList.json';
import { END_POINTS } from '@/routes';
import { Category } from '@/types';

export const categoryHandlers = [
  http.get(`${API_URL}${END_POINTS.CATEGORIES}`, () => HttpResponse.json(mockCategoryList)),
  http.post(`${API_URL}${END_POINTS.CATEGORIES}`, async (req) => {
    const newCategory = await req.request.json();

    if (typeof newCategory === 'object' && newCategory !== null) {
      const newId = mockCategoryList.categories.length + 1;
      const category = { id: newId, ...newCategory } as Category;

      mockCategoryList.categories.push(category);

      return HttpResponse.json({ status: 201, category });
    }

    return HttpResponse.json({ status: 400, message: 'Invalid category data' });
  }),
  http.put(`${API_URL}${END_POINTS.CATEGORIES}/:id`, async (req) => {
    const { id } = req.params;
    const updatedCategory = await req.request.json();
    const categoryIndex = mockCategoryList.categories.findIndex((cat) => cat.id.toString() === id);

    if (categoryIndex !== -1 && typeof updatedCategory === 'object' && updatedCategory !== null) {
      mockCategoryList.categories[categoryIndex] = { id: parseInt(id as string), ...updatedCategory } as Category;

      return HttpResponse.json({ status: 200, category: mockCategoryList.categories[categoryIndex] });
    } else {
      return HttpResponse.json({ status: 404, message: 'Category not found or invalid data' });
    }
  }),
  http.delete(`${API_URL}${END_POINTS.CATEGORIES}/:id`, (req) => {
    const { id } = req.params;
    const categoryIndex = mockCategoryList.categories.findIndex((cat) => cat.id.toString() === id);

    if (categoryIndex !== -1) {
      mockCategoryList.categories.splice(categoryIndex, 1);

      return new HttpResponse(null, {
        status: 204,
      });
    } else {
      return HttpResponse.json({ status: 404, message: 'Category not found' });
    }
  }),
];
