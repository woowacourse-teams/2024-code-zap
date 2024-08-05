import { HttpResponse, http } from 'msw';

import { CATEGORY_API_URL } from '@/api';
import { TEMPLATE_API_URL } from '@/api/templates';
import mockCategoryList from './categoryList.json';
import mockTemplate from './template.json';
import mockTemplateList from './templateList.json';

const templateHandlers = [
  http.get(`${TEMPLATE_API_URL}`, () => {
    const response = HttpResponse.json(mockTemplateList);

    return response;
  }),
  http.get(`${TEMPLATE_API_URL}/:id`, () => {
    const response = HttpResponse.json(mockTemplate);

    return response;
  }),
  http.post(`${TEMPLATE_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.post(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 200 })),
  http.delete(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 204 })),
];

const categoryHandlers = [
  http.get(`${CATEGORY_API_URL}`, () => {
    const response = HttpResponse.json(mockCategoryList);

    return response;
  }),
  http.post(`${CATEGORY_API_URL}`, async () => HttpResponse.json({ status: 201 })),
];

export const handlers = [...templateHandlers, ...categoryHandlers];
