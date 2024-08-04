import { HttpResponse, http } from 'msw';

import { CATEGORY_API_URL, TEMPLATE_API_URL } from '@/api';
import mockCategoryList from './categoryList.json';
import mockTemplate from './template.json';
import mockTemplateList from './templateList.json';

export const handlers = [
  http.get(`${TEMPLATE_API_URL}`, () => HttpResponse.json(mockTemplateList)),
  http.get(`${TEMPLATE_API_URL}/:id`, () => HttpResponse.json(mockTemplate)),
  http.post(`${TEMPLATE_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.post(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 200 })),
  http.delete(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 204 })),
  http.get(`${CATEGORY_API_URL}`, () => HttpResponse.json(mockCategoryList)),
];
