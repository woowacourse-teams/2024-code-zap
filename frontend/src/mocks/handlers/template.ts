import { HttpResponse, http } from 'msw';

import { API_URL } from '@/api';
import mockTemplateList from '@/mocks/fixtures/templateList.json';
import { END_POINTS } from '@/routes';

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
