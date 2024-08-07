import { HttpResponse, http } from 'msw';

import {
  CATEGORY_API_URL,
  TEMPLATE_API_URL,
  CHECK_EMAIL_API_URL,
  CHECK_USERNAME_API_URL,
  LOGIN_API_URL,
  LOGIN_STATE_API_URL,
  LOGOUT_API_URL,
  SIGNUP_API_URL,
} from '@/api';
import mockCategoryList from './categoryList.json';
import mockTemplate from './template.json';
import mockTemplateList from './templateList.json';

export const templateHandlers = [
  http.get(`${TEMPLATE_API_URL}`, (req) => {
    const url = new URL(req.request.url);
    const keyword = url.searchParams.get('keyword');
    const categoryId = url.searchParams.get('categoryId');
    const tagIds = url.searchParams.get('tagIds');
    const page = parseInt(url.searchParams.get('page') || '1', 10);
    const pageSize = parseInt(url.searchParams.get('pageSize') || '20', 10);

    let filteredTemplates = mockTemplateList.templates;

    if (keyword) {
      filteredTemplates = filteredTemplates.filter(
        (template) =>
          template.title.includes(keyword) ||
          template.description.includes(keyword) ||
          template.snippets.some((snippet) => snippet.content.includes(keyword)),
      );
    }

    if (categoryId) {
      filteredTemplates = filteredTemplates.filter((template) => template.category.id.toString() === categoryId);
    }

    if (tagIds) {
      filteredTemplates = filteredTemplates.filter((template) =>
        template.tags.some((tag) => tagIds.split(',').includes(tag.id.toString())),
      );
    }

    const totalElements = filteredTemplates.length;
    const totalPages = Math.ceil(totalElements / pageSize);
    const startIndex = (page - 1) * pageSize;
    const endIndex = startIndex + pageSize;
    const paginatedTemplates = filteredTemplates.slice(startIndex, endIndex);
    const numberOfElements = paginatedTemplates.length;

    return HttpResponse.json({
      templates: paginatedTemplates,
      totalPages,
      totalElements,
      numberOfElements,
    });
  }),
  http.get(`${TEMPLATE_API_URL}/:id`, () => HttpResponse.json(mockTemplate)),
  http.post(`${TEMPLATE_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.post(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 200 })),
  http.delete(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 204 })),
];

const authenticationHandler = [
  http.post(`${SIGNUP_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.post(
    `${LOGIN_API_URL}`,
    () =>
      new HttpResponse(null, {
        status: 200,
      }),
  ),
  http.post(
    `${LOGOUT_API_URL}`,
    () =>
      new HttpResponse(null, {
        status: 204,
        statusText: 'AUTHORIZED',
      }),
  ),
  http.get(
    `${CHECK_EMAIL_API_URL}`,
    () =>
      new HttpResponse(
        JSON.stringify({
          message: '중복된 이메일입니다.',
          status: 409,
        }),
        {
          status: 409,
          statusText: 'Conflict',
        },
      ),
  ),
  http.get(
    `${CHECK_USERNAME_API_URL}`,
    () =>
      new HttpResponse(
        JSON.stringify({
          message: '사용 가능한 사용자 이름입니다.',
          status: 200,
          ok: true,
        }),
        {
          status: 200,
          statusText: 'Conflict',
        },
      ),
  ),
  http.get(
    `${LOGIN_STATE_API_URL}`,
    () =>
      new HttpResponse(
        JSON.stringify({
          ok: true,
          // message: '인증되지 않은 사용자입니다.',
          message: '인증완료',
          status: 200,
        }),
        {
          status: 200,
          // statusText: 'UNAUTHORIZED',
          statusText: 'AUTHORIZED',
        },
      ),
  ),
];

const categoryHandlers = [
  http.get(`${CATEGORY_API_URL}`, () => HttpResponse.json(mockCategoryList)),
  http.post(`${CATEGORY_API_URL}`, async () => HttpResponse.json({ status: 201 })),
];

export const handlers = [...templateHandlers, ...categoryHandlers, ...authenticationHandler];
