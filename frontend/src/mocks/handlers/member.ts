import { HttpResponse, http } from 'msw';

import { API_URL } from '@/api';
import { END_POINTS } from '@/routes';

export const memberHandlers = [
  http.get(`${API_URL}${END_POINTS.MEMBERS}/:id/name`, () => HttpResponse.json({ name: 'll' })),
];
