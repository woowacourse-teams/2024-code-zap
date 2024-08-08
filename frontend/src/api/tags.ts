import { TagListResponse } from '@/types/api';
import { MemberInfo } from '@/types/authentication';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const TAG_API_URL = `${API_URL}/tags`;

export const getTagList = async ({ memberId }: Pick<MemberInfo, 'memberId'>): Promise<TagListResponse> => {
  const url = new URL(TAG_API_URL);

  url.searchParams.append('memberId', String(memberId));

  return await customFetch({
    url: url.toString(),
  });
};
