import { HttpResponse, http } from 'msw';

import { API_URL } from '@/api';
import { END_POINTS } from '@/routes';
import { mockResponse } from '@/utils/mockResponse';

export const authenticationHandler = [
  http.post(`${API_URL}${END_POINTS.SIGNUP}`, async () => HttpResponse.json({ status: 201 })),

  http.post(`${API_URL}${END_POINTS.LOGIN}`, () =>
    mockResponse({
      status: 200,
      body: { memberId: 1, name: 'jay' },
      headers: {
        'Content-Type': 'application/json',
        'Set-Cookie': 'Authorization=abc123; HttpOnly; Path=/; Max-Age=3600',
      },
    }),
  ),

  http.post(`${API_URL}${END_POINTS.LOGOUT}`, () =>
    mockResponse({
      status: 204,
      body: {
        statusText: 'AUTHORIZED',
      },
    }),
  ),

  http.get(`${API_URL}${END_POINTS.CHECK_NAME}`, () =>
    mockResponse({
      status: 200,
      body: {
        message: '사용 가능한 아이디입니다.',
        status: 200,
        ok: true,
      },
    }),
  ),

  http.get(`${API_URL}${END_POINTS.LOGIN_CHECK}`, () =>
    mockResponse({
      status: 200,
      body: {
        ok: true,
        message: '인증완료',
        status: 200,
      },
    }),
  ),
];
