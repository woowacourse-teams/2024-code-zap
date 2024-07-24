import { HttpResponse, http } from 'msw';
import { TEMPLATE_API_URL } from '@/api/templates';
import mockTemplate from './template.json';
import mockTemplateList from './templateList.json';

export const handlers = [
  http.get(`${TEMPLATE_API_URL}`, () => {
    const response = HttpResponse.json(mockTemplateList);

    return response;
  }),
  http.get(`${TEMPLATE_API_URL}/:id`, () => {
    const response = HttpResponse.json(mockTemplate);

    return response;
  }),
  http.post(`${TEMPLATE_API_URL}`, async () => HttpResponse.json({ status: 201 })),
];
