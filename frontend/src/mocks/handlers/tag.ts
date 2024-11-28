import { HttpResponse, http } from 'msw';

import { API_URL } from '@/api';
import mockTagList from '@/mocks/fixtures/tagList.json';
import { END_POINTS } from '@/routes';

export const tagHandlers = [http.get(`${API_URL}${END_POINTS.TAGS}`, () => HttpResponse.json(mockTagList))];
