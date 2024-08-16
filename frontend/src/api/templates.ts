import type {
  Template,
  TemplateEditRequest,
  TemplateListResponse,
  TemplateUploadRequest,
  TemplateListRequest,
} from '@/types';
import { SortingOption } from '@/types';
import { customFetch } from './customFetch';

const API_URL = process.env.REACT_APP_API_URL;

export const TEMPLATE_API_URL = `${API_URL}/templates`;

export const PAGE_SIZE = 20;

export const SORTING_OPTIONS: SortingOption[] = [
  {
    key: 'modifiedAt,desc',
    value: '최근 순',
  },
  {
    key: 'modifiedAt,asc',
    value: '오래된 순',
  },
];

export const DEFAULT_SORTING_OPTION = SORTING_OPTIONS[0];

export const getTemplateList = async ({
  keyword = '',
  categoryId,
  tagIds,
  sort = DEFAULT_SORTING_OPTION.key,
  page = 1,
  pageSize = PAGE_SIZE,
  memberId,
}: TemplateListRequest) => {
  const queryParams = new URLSearchParams({
    keyword,
    memberId: String(memberId),
    sort,
    pageNumber: page.toString(),
    pageSize: pageSize.toString(),
  });

  if (categoryId) {
    queryParams.append('categoryId', categoryId.toString());
  }

  if (tagIds?.length !== 0 && tagIds !== undefined) {
    queryParams.append('tagIds', tagIds.toString());
  }

  const url = `${TEMPLATE_API_URL}?${queryParams.toString()}`;

  const response = await customFetch<TemplateListResponse>({
    url,
  });

  if ('templates' in response) {
    return response;
  }

  throw new Error(response.detail);
};

export const getTemplate = async (id: number) => {
  const response = await customFetch<Template>({
    url: `${TEMPLATE_API_URL}/${id}`,
  });

  if ('snippets' in response) {
    return response;
  }

  throw new Error(response.detail);
};

export const postTemplate = async (newTemplate: TemplateUploadRequest) => {
  const response = await customFetch({
    method: 'POST',
    url: `${TEMPLATE_API_URL}`,
    body: JSON.stringify(newTemplate),
  });

  return response;
};

export const editTemplate = async ({ id, template }: { id: number; template: TemplateEditRequest }): Promise<void> => {
  await customFetch({
    method: 'POST',
    url: `${TEMPLATE_API_URL}/${id}`,
    body: JSON.stringify(template),
  });
};

export const deleteTemplate = async (ids: number[]): Promise<void> => {
  await customFetch({
    method: 'DELETE',
    url: `${TEMPLATE_API_URL}/${ids.join(',')}`,
  });
};
