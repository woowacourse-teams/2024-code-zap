import { END_POINTS } from '@/routes';

import { apiClient } from './config';

const API_URL = process.env.REACT_APP_API_URL || 'https://default-url.com';

export const MEMBER_API_URL = `${API_URL}${END_POINTS.MEMBERS}`;

export const getMemberName = async (memberId: number) => {
  const response = await apiClient.get(`${END_POINTS.MEMBERS}/${memberId}/name`);

  return await response.json();
};
