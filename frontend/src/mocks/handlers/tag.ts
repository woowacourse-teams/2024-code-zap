import { http } from 'msw';

import { API_URL } from '@/api';
import mockTagList from '@/mocks/fixtures/tagList.json';
import { END_POINTS } from '@/routes';
import { mockResponse } from '@/utils/mockResponse';

export const tagHandlers = [
  http.get(`${API_URL}${END_POINTS.TAGS}`, () =>
    mockResponse({
      status: 200,
      body: mockTagList,
    }),
  ),
];
