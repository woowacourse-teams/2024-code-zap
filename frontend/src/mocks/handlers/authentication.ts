import { HttpResponse, http } from 'msw';

import { API_URL } from '@/api';
import { END_POINTS } from '@/routes';

export const authenticationHandler = [
  http.post(`${API_URL}${END_POINTS.SIGNUP}`, async () => HttpResponse.json({ status: 201 })),
  http.post(
    `${API_URL}${END_POINTS.LOGIN}`,
    () =>
      new HttpResponse(JSON.stringify({ memberId: 1, name: 'jay' }), {
        status: 200,
        headers: {
          'Content-Type': 'application/json',
          'Set-Cookie': 'Authorization=abc123; HttpOnly; Path=/; Max-Age=3600',
        },
      }),
  ),
  http.post(
    `${API_URL}${END_POINTS.LOGOUT}`,
    () =>
      new HttpResponse(null, {
        status: 204,
        statusText: 'AUTHORIZED',
      }),
  ),
  http.get(
    `${API_URL}${END_POINTS.CHECK_NAME}`,
    () =>
      new HttpResponse(
        JSON.stringify({
          message: '사용 가능한 아이디입니다.',
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
    `${API_URL}${END_POINTS.LOGIN_CHECK}`,
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
