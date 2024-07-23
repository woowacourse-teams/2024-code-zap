import { HttpResponse, http } from 'msw';
import { API_URL, END_POINT } from '@/api/config';
import mockTemplate from './template.json';
import mockTemplateList from './templateList.json';

export const handlers = [
  http.get(`${API_URL}${END_POINT.TEMPLATES}`, () => {
    const response = HttpResponse.json(mockTemplateList);

    return response;
  }),
  http.get(`${API_URL}${END_POINT.TEMPLATES}/:id`, () => {
    const response = HttpResponse.json(mockTemplate);

    return response;
  }),
  http.post(`${API_URL}${END_POINT.TEMPLATES}`, async () => HttpResponse.json({ status: 201 })),
];
