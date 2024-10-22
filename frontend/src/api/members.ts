import { END_POINTS } from '@/routes';
import { GetMemberNameResponse } from '@/types';

import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const MEMBER_API_URL = `${API_URL}${END_POINTS.MEMBERS}`;

export const getMemberName = async (memberId: number) => {
  const url = `${MEMBER_API_URL}/${memberId}/name`;

  const response = await customFetch<GetMemberNameResponse>({
    url,
  });

  if ('name' in response) {
    return response;
  }

  throw new Error(response.detail);
};
