import { HttpResponse, http } from 'msw';

import { CATEGORY_API_URL } from '@/api';
import {
  CHECK_EMAIL_API_URL,
  CHECK_USERNAME_API_URL,
  LOGIN_API_URL,
  LOGIN_STATE_API_URL,
  SIGNUP_API_URL,
} from '@/api/authentication';
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

const authenticationHandler = [
  http.post(`${SIGNUP_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.post(
    `${LOGIN_API_URL}`,
    () =>
      new HttpResponse(null, {
        status: 200,
      }),
  ),
  http.get(
    `${CHECK_EMAIL_API_URL}`,
    () =>
      new HttpResponse(
        JSON.stringify({
          message: '중복된 이메일입니다.',
          status: 409,
        }),
        {
          status: 409,
          statusText: 'Conflict',
        },
      ),
  ),
  http.get(
    `${CHECK_USERNAME_API_URL}`,
    () =>
      new HttpResponse(
        JSON.stringify({
          message: '사용 가능한 사용자 이름입니다.',
          status: 200,
          ok: true,
        }),
        {
          status: 200,
          statusText: 'Conflict',
        },
      ),
  ),
  http.get(
    `${LOGIN_STATE_API_URL}`,
    () =>
      new HttpResponse(
        JSON.stringify({
          ok: true,
          // message: '인증되지 않은 사용자입니다.',
          message: '인증완료',
          status: 200,
        }),
        {
          status: 200,
          // statusText: 'UNAUTHORIZED',
          statusText: 'AUTHORIZED',
        },
      ),
  ),
];

const categoryHandlers = [
  http.get(`${CATEGORY_API_URL}`, () => {
    const response = HttpResponse.json(mockCategoryList);

    return response;
  }),
  http.post(`${CATEGORY_API_URL}`, async () => HttpResponse.json({ status: 201 })),
];

export const handlers = [...templateHandlers, ...categoryHandlers, ...authenticationHandler];
