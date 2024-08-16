import { HttpResponse, http } from 'msw';

import {
  CATEGORY_API_URL,
  TEMPLATE_API_URL,
  CHECK_NAME_API_URL,
  LOGIN_API_URL,
  LOGIN_STATE_API_URL,
  LOGOUT_API_URL,
  SIGNUP_API_URL,
} from '@/api';
import { TAG_API_URL } from '../api/tags';
import mockCategoryList from './categoryList.json';
import mockTagList from './tagList.json';
import mockTemplateList from './templateList.json';

export const templateHandlers = [
  http.get(`${TEMPLATE_API_URL}`, (req) => {
    const url = new URL(req.request.url);
    const keyword = url.searchParams.get('keyword');
    const categoryId = url.searchParams.get('categoryId');
    const tagIds = url.searchParams.get('tagIds');
    const sort = url.searchParams.get('sort');
    const page = parseInt(url.searchParams.get('page') || '1', 10);
    const pageSize = parseInt(url.searchParams.get('pageSize') || '20', 10);

    let filteredTemplates = mockTemplateList.templates;

    if (keyword) {
      filteredTemplates = filteredTemplates.filter(
        (template) =>
          template.title.includes(keyword) ||
          template.description.includes(keyword) ||
          template.sourceCodes.some((sourceCode) => sourceCode.content.includes(keyword)),
      );
    }

    if (categoryId) {
      filteredTemplates = filteredTemplates.filter((template) => template.category.id.toString() === categoryId);
    }

    if (tagIds) {
      filteredTemplates = filteredTemplates.filter((template) =>
        tagIds.split(',').every((tagId) => template.tags.some((tag) => tag.id.toString() === tagId)),
      );
    }

    switch (sort) {
      case 'modifiedAt,asc':
        filteredTemplates.sort((a, b) => new Date(a.modifiedAt).getTime() - new Date(b.modifiedAt).getTime());
        break;

      case 'modifiedAt,desc':
        filteredTemplates.sort((a, b) => new Date(b.modifiedAt).getTime() - new Date(a.modifiedAt).getTime());
        break;

      default:
        break;
    }

    const totalElements = filteredTemplates.length;
    const totalPages = Math.ceil(totalElements / pageSize);
    const startIndex = (page - 1) * pageSize;
    const endIndex = startIndex + pageSize;
    const paginatedTemplates = filteredTemplates.slice(startIndex, endIndex);
    const numberOfElements = paginatedTemplates.length;

    return HttpResponse.json({
      status: 200,
      templates: paginatedTemplates,
      totalPages,
      totalElements,
      numberOfElements,
    });
  }),
  http.get(`${TEMPLATE_API_URL}/:id`, (req) => {
    const { id } = req.params;

    const template = mockTemplateList.templates.find((template) => template.id.toString() === id);

    if (template) {
      return HttpResponse.json(template);
    } else {
      return HttpResponse.json({ status: 404, message: 'Template not found' });
    }
  }),
  http.post(`${TEMPLATE_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.post(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 200 })),
  http.delete(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 204 })),
];

const authenticationHandler = [
  http.post(`${SIGNUP_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.post(
    `${LOGIN_API_URL}`,
    () =>
      new HttpResponse(JSON.stringify({ memberId: 1, name: 'jay' }), {
        status: 200,
        headers: {
          'Content-Type': 'application/json',
          'Set-Cookie': 'Authorization=abc123; HttpOnly; Path=/; Max-Age=3600',
        },
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
    `${CHECK_NAME_API_URL}`,
    () =>
      new HttpResponse(
        JSON.stringify({
          message: '사용 가능한 아이디입니다.',
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

const tagHandlers = [http.get(`${TAG_API_URL}`, () => HttpResponse.json(mockTagList))];

export const handlers = [...tagHandlers, ...templateHandlers, ...categoryHandlers, ...authenticationHandler];
