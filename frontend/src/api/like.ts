import { END_POINTS } from '@/routes';

import { apiClient } from './config';

export const postLike = async (templateId: number) => await apiClient.post(`${END_POINTS.LIKES}/${templateId}`, {});

export const deleteLike = async (templateId: number) => await apiClient.delete(`${END_POINTS.LIKES}/${templateId}`);
