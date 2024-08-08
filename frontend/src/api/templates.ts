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
    key: 'createdAt,desc',
    value: '최근 순',
  },
  {
    key: 'createdAt,asc',
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
}: TemplateListRequest): Promise<TemplateListResponse> => {
  const url = new URL(TEMPLATE_API_URL);

  url.searchParams.append('keyword', keyword);
  url.searchParams.append('memberId', String(memberId));

  if (categoryId) {
    url.searchParams.append('categoryId', categoryId.toString());
  }

  if (tagIds) {
    url.searchParams.append('tagIds', tagIds.toString());
  }

  url.searchParams.append('sort', sort);
  url.searchParams.append('pageNumber', page.toString());
  url.searchParams.append('pageSize', pageSize.toString());

  return await customFetch({
    url: url.toString(),
  });
};

export const getTemplate = async (id: number): Promise<Template> =>
  await customFetch({
    url: `${TEMPLATE_API_URL}/${id}`,
  });

export const postTemplate = async (newTemplate: TemplateUploadRequest) =>
  await customFetch({
    method: 'POST',
    url: `${TEMPLATE_API_URL}`,
    body: JSON.stringify(newTemplate),
  });

export const editTemplate = async ({ id, template }: { id: number; template: TemplateEditRequest }): Promise<void> => {
  await customFetch({
    method: 'POST',
    url: `${TEMPLATE_API_URL}/${id}`,
    body: JSON.stringify(template),
  });
};

export const deleteTemplate = async (id: number): Promise<void> => {
  await customFetch({
    method: 'DELETE',
    url: `${TEMPLATE_API_URL}/${id}`,
  });
};
