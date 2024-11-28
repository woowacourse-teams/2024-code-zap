import { http } from 'msw';

import { API_URL } from '@/api';
import { END_POINTS } from '@/routes';
import { mockResponse } from '@/utils/mockResponse';

export const memberHandlers = [
  http.get(`${API_URL}${END_POINTS.MEMBERS}/:id/name`, () =>
    mockResponse({
      status: 200,
      body: {
        name: 'll',
      },
    }),
  ),
];
