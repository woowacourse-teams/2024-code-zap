import { TagListResponse } from '@/types/api';
import { MemberInfo } from '@/types/authentication';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const TAG_API_URL = `${API_URL}/templates/tags`;

export const getTagList = async ({ memberId }: Pick<MemberInfo, 'memberId'>) => {
  const url = new URL(TAG_API_URL);

  url.searchParams.append('memberId', String(memberId));

  const response = await customFetch<TagListResponse>({
    url: url.toString(),
  });

  if ('tags' in response) {
    return response;
  }

  throw new Error(response.detail);
};
