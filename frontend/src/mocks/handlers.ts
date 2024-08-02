import { HttpResponse, http } from 'msw';

import { CHECK_EMAIL_API_URL, CHECK_USERNAME_API_URL, LOGIN_API_URL, SIGNUP_API_URL } from '@/api/authentication';
import { TEMPLATE_API_URL } from '@/api/templates';
import mockTemplate from './template.json';
import mockTemplateList from './templateList.json';
import mockTemplateList_2 from './templateList_2.json';

const mockToken =
  'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c';

const mockLogin = (token: string) => {
  if (token === `Bearer Bearer ${mockToken}`) {
    return mockTemplateList_2;
  } else {
    return mockTemplateList;
  }
};

export const handlers = [
  // templates
  http.get(`${TEMPLATE_API_URL}`, ({ request }) => {
    const token = request.headers.get('Authorization') || '';

    return HttpResponse.json(mockLogin(token));
  }),
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
        headers: {
          Authorization: `Bearer ${mockToken}`,
        },
      }),
  ),
  http.get(`${CHECK_EMAIL_API_URL}`, async () => HttpResponse.json({ check: true })),
  http.get(`${CHECK_USERNAME_API_URL}`, async () => HttpResponse.json({ check: true })),
];
