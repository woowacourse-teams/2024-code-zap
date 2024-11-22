import { HttpResponse, http } from 'msw';

import { API_URL } from '@/api';
import { END_POINTS } from '@/routes';
import { Category } from '@/types';

import mockCategoryList from './categoryList.json';
import mockTagList from './tagList.json';
import mockTemplateList from './templateList.json';

export const templateHandlers = [
  http.get(`${API_URL}${END_POINTS.TEMPLATES_EXPLORE}`, (req) => {
    const url = new URL(req.request.url);
    const keyword = url.searchParams.get('keyword');
    const categoryId = url.searchParams.get('categoryId');
    const tagIds = url.searchParams.get('tagIds');
    const sort = url.searchParams.get('sort');
    const page = parseInt(url.searchParams.get('page') || '1', 10);
    const size = parseInt(url.searchParams.get('size') || '20', 10);

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

    const paginationSizes = Math.min(Math.ceil(filteredTemplates.length / size - page + 1), 5);
    const startIndex = (page - 1) * size;
    const endIndex = startIndex + size;
    const paginatedTemplates = filteredTemplates.slice(startIndex, endIndex);
    const numberOfElements = paginatedTemplates.length;

    return HttpResponse.json({
      status: 200,
      templates: paginatedTemplates,
      paginationSizes,
      numberOfElements,
    });
  }),
  http.get(`${API_URL}${END_POINTS.TEMPLATES_EXPLORE}/:id`, (req) => {
    const { id } = req.params;

    const template = mockTemplateList.templates.find((template) => template.id.toString() === id);

    if (template) {
      return HttpResponse.json(template);
    } else {
      return HttpResponse.json({ status: 404, message: 'Template not found' });
    }
  }),
  http.post(`${API_URL}${END_POINTS.TEMPLATES_EXPLORE}`, async () => HttpResponse.json({ status: 201 })),
  http.post(`${API_URL}${END_POINTS.TEMPLATES_EXPLORE}/:id`, async () => HttpResponse.json({ status: 200 })),
  http.delete(`${API_URL}${END_POINTS.TEMPLATES_EXPLORE}/:id`, async () => HttpResponse.json({ status: 204 })),
];

const authenticationHandler = [
  http.post(`${API_URL}${END_POINTS.SIGNUP}`, async () => HttpResponse.json({ status: 201 })),
  http.post(
    `${API_URL}${END_POINTS.LOGIN}`,
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
    `${API_URL}${END_POINTS.LOGOUT}`,
    () =>
      new HttpResponse(null, {
        status: 204,
        statusText: 'AUTHORIZED',
      }),
  ),
  http.get(
    `${API_URL}${END_POINTS.CHECK_NAME}`,
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
    `${API_URL}${END_POINTS.LOGIN_CHECK}`,
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
  http.get(`${API_URL}${END_POINTS.CATEGORIES}`, () => HttpResponse.json(mockCategoryList)),
  http.post(`${API_URL}${END_POINTS.CATEGORIES}`, async (req) => {
    const newCategory = await req.request.json();

    if (typeof newCategory === 'object' && newCategory !== null) {
      const newId = mockCategoryList.categories.length + 1;
      const category = { id: newId, ...newCategory } as Category;

      mockCategoryList.categories.push(category);

      return HttpResponse.json({ status: 201, category });
    }

    return HttpResponse.json({ status: 400, message: 'Invalid category data' });
  }),
  http.put(`${API_URL}${END_POINTS.CATEGORIES}/:id`, async (req) => {
    const { id } = req.params;
    const updatedCategory = await req.request.json();
    const categoryIndex = mockCategoryList.categories.findIndex((cat) => cat.id.toString() === id);

    if (categoryIndex !== -1 && typeof updatedCategory === 'object' && updatedCategory !== null) {
      mockCategoryList.categories[categoryIndex] = { id: parseInt(id as string), ...updatedCategory } as Category;

      return HttpResponse.json({ status: 200, category: mockCategoryList.categories[categoryIndex] });
    } else {
      return HttpResponse.json({ status: 404, message: 'Category not found or invalid data' });
    }
  }),
  http.delete(`${API_URL}${END_POINTS.CATEGORIES}/:id`, (req) => {
    const { id } = req.params;
    const categoryIndex = mockCategoryList.categories.findIndex((cat) => cat.id.toString() === id);

    if (categoryIndex !== -1) {
      mockCategoryList.categories.splice(categoryIndex, 1);

      return new HttpResponse(null, {
        status: 204,
      });
    } else {
      return HttpResponse.json({ status: 404, message: 'Category not found' });
    }
  }),
];

const tagHandlers = [http.get(`${API_URL}${END_POINTS.TAGS}`, () => HttpResponse.json(mockTagList))];

const likeHandlers = [
  http.post(`${API_URL}${END_POINTS.LIKES}/:templateId`, (req) => {
    const { templateId } = req.params;
    const template = mockTemplateList.templates.find((temp) => temp.id.toString() === templateId);

    if (!template) {
      return HttpResponse.json({ status: 404, message: 'Template not found' });
    }

    if (template.isLiked) {
      return HttpResponse.json({ status: 400, message: 'Already liked' });
    }

    template.isLiked = true;
    template.likesCount += 1;

    return HttpResponse.json({
      status: 200,
      message: 'Liked successfully',
      likesCount: template.likesCount,
      isLiked: template.isLiked,
    });
  }),

  http.delete(`${API_URL}${END_POINTS.LIKES}/:templateId`, (req) => {
    const { templateId } = req.params;
    const template = mockTemplateList.templates.find((temp) => temp.id.toString() === templateId);

    if (!template) {
      return HttpResponse.json({ status: 404, message: 'Template not found' });
    }

    if (!template.isLiked) {
      return HttpResponse.json({ status: 400, message: 'Not liked yet' });
    }

    template.isLiked = false;
    template.likesCount -= 1;

    return HttpResponse.json({
      status: 200,
      message: 'Disliked successfully',
      likesCount: template.likesCount,
      isLiked: template.isLiked,
    });
  }),
];

const memberHandlers = [http.get(`${API_URL}${END_POINTS.MEMBERS}/:id/name`, () => HttpResponse.json({ name: 'll' }))];

export const handlers = [
  ...tagHandlers,
  ...templateHandlers,
  ...categoryHandlers,
  ...authenticationHandler,
  ...likeHandlers,
  ...memberHandlers,
];
