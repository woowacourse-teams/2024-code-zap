import { apiClient } from '@/api/config';
import { END_POINTS } from '@/routes';

export const postLike = async (templateId: number) => await apiClient.post(`${END_POINTS.LIKES}/${templateId}`, {});

export const deleteLike = async (templateId: number) => await apiClient.delete(`${END_POINTS.LIKES}/${templateId}`);
