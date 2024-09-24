import { HttpResponse } from 'msw';

import { END_POINTS } from '@/routes';
import type { CustomError } from '@/types';
import { LikeDeleteRequest, LikePostRequest } from '@/types';

import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const LIKE_API_URL = `${API_URL}${END_POINTS.LIKES}`;

export const postLike = async ({ templateId }: LikePostRequest) => {
  const response = await customFetch<HttpResponse>({
    method: 'POST',
    url: `${LIKE_API_URL}/${templateId}`,
  });

  if (response.status >= 400) {
    throw response as CustomError;
  }

  return response;
};

export const deleteLike = async ({ templateId }: LikeDeleteRequest) => {
  const response = await customFetch<HttpResponse>({
    method: 'DELETE',
    url: `${LIKE_API_URL}/${templateId}`,
  });

  if (response.status >= 400) {
    throw response as CustomError;
  }

  return response;
};
