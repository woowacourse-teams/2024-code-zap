import { http } from 'msw';

import { API_URL } from '@/api';
import mockTemplateList from '@/mocks/fixtures/templateList.json';
import { END_POINTS } from '@/routes';
import { mockResponse } from '@/utils/mockResponse';

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
      case 'createdAt,asc':
        filteredTemplates.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
        break;

      case 'createdAt,desc':
        filteredTemplates.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
        break;

      case 'modifiedAt,desc':
        filteredTemplates.sort((a, b) => new Date(b.modifiedAt).getTime() - new Date(a.modifiedAt).getTime());
        break;

      case 'likesCount,desc':
        filteredTemplates.sort((a, b) => new Date(b.likesCount).getTime() - new Date(a.likesCount).getTime());
        break;

      default:
        break;
    }

    const paginationSizes = Math.min(Math.ceil(filteredTemplates.length / size - page + 1), 5);
    const startIndex = (page - 1) * size;
    const endIndex = startIndex + size;
    const paginatedTemplates = filteredTemplates.slice(startIndex, endIndex);
    const numberOfElements = paginatedTemplates.length;

    return mockResponse({
      status: 200,
      body: {
        templates: paginatedTemplates,
        paginationSizes,
        numberOfElements,
      },
    });
  }),

  http.get(`${API_URL}${END_POINTS.TEMPLATES_EXPLORE}/:id`, (req) => {
    const { id } = req.params;

    const template = mockTemplateList.templates.find((template) => template.id.toString() === id);

    if (template) {
      return mockResponse({ status: 200, body: template });
    }

    return mockResponse({
      status: 404,
      errorBody: {
        errorCode: 404,
        instance: END_POINTS.TEMPLATES_EXPLORE,
        detail: 'Template not found',
      },
    });
  }),

  http.post(`${API_URL}${END_POINTS.TEMPLATES_EXPLORE}`, async () => mockResponse({ status: 201 })),

  http.post(`${API_URL}${END_POINTS.TEMPLATES_EXPLORE}/:id`, async () => mockResponse({ status: 200 })),

  http.delete(`${API_URL}${END_POINTS.TEMPLATES_EXPLORE}/:id`, async () => mockResponse({ status: 200 })),
];
