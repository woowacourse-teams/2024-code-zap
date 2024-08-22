import { MemberInfo } from '@/types';
import { TagListResponse } from '@/types/api';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const TAG_API_URL = `${API_URL}/tags`;

export const getTagList = async ({ memberId }: Pick<MemberInfo, 'memberId'>) => {
  const url = `${TAG_API_URL}?memberId=${memberId}`;

  const response = await customFetch<TagListResponse>({
    url,
  });

  if ('tags' in response) {
    return response;
  }

  throw new Error(response.detail);
};
