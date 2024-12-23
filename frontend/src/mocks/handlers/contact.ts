import { http } from 'msw';

import { API_URL } from '@/api';
import { END_POINTS } from '@/routes';
import { mockResponse } from '@/utils/mockResponse';

export const contactHandlers = [http.get(`${API_URL}${END_POINTS.CONTACT}`, () => mockResponse({ status: 201 }))];
