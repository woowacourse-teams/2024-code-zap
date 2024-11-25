import { apiClient } from '@/api/config';
import { END_POINTS } from '@/routes';

export const getMemberName = async (memberId: number) => {
  const response = await apiClient.get(`${END_POINTS.MEMBERS}/${memberId}/name`);

  return await response.json();
};
