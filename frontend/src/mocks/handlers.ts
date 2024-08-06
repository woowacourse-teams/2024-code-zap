import { HttpResponse, http } from 'msw';

import {
  CHECK_EMAIL_API_URL,
  CHECK_USERNAME_API_URL,
  LOGIN_API_URL,
  LOGIN_STATE_API_URL,
  LOGOUT_API_URL,
  SIGNUP_API_URL,
} from '@/api/authentication';
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
  http.post(
    `${LOGIN_API_URL}`,
    () =>
      new HttpResponse(null, {
        status: 200,
      }),
  ),
  http.post(
    `${LOGOUT_API_URL}`,
    () =>
      new HttpResponse(null, {
        status: 204,
        statusText: 'AUTHORIZED',
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
