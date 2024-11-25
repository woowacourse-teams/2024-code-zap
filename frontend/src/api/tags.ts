import { apiClient } from '@/api/config';
import { END_POINTS } from '@/routes';

export const getTagList = async (memberId: number) => {
  const queryParams = new URLSearchParams({
    memberId: memberId.toString(),
  });
  const response = await apiClient.get(`${END_POINTS.TAGS}?${queryParams.toString()}`);

  return await response.json();
};
