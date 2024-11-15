import { END_POINTS } from '@/routes';
import { TagListResponse } from '@/types/api';

import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL || 'https://default-url.com';

export const TAG_API_URL = `${API_URL}${END_POINTS.TAGS}`;

export const getTagList = async (memberId: number) => {
  const url = `${TAG_API_URL}?memberId=${memberId}`;

  const response = await customFetch<TagListResponse>({
    url,
  });

  if ('tags' in response) {
    return response;
  }

  throw new Error(response.detail);
};
