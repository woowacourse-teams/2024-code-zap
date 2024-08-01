import { HttpResponse, http } from 'msw';

import { CHECK_EMAIL_API_URL, CHECK_USERNAME_API_URL, SIGNUP_API_URL } from '@/api/authentication';
import { TEMPLATE_API_URL } from '@/api/templates';
import mockTemplate from './template.json';
import mockTemplateList from './templateList.json';

export const handlers = [
  // templates
  http.get(`${TEMPLATE_API_URL}`, () => HttpResponse.json(mockTemplateList)),
  http.get(`${TEMPLATE_API_URL}/:id`, () => HttpResponse.json(mockTemplate)),
  http.post(`${TEMPLATE_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.post(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 200 })),
  http.delete(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 204 })),

  // authentication
  http.post(`${SIGNUP_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.get(`${CHECK_EMAIL_API_URL}`, async () => HttpResponse.json({ check: true })),
  http.get(`${CHECK_USERNAME_API_URL}`, async () => HttpResponse.json({ check: true })),
];
