import { END_POINTS } from '@/routes';

import { apiClient } from './config';

export interface ContactBody {
  message: string;
  email: string | null;
  name: string | null;
  memberId: number | null;
}

export const postContact = async (contactBody: ContactBody) =>
  await apiClient.post(`${END_POINTS.CONTACT}`, contactBody);
