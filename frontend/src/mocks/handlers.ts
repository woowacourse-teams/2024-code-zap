import { HttpResponse, http } from 'msw';

import { CATEGORY_API_URL, TEMPLATE_API_URL } from '@/api';
import mockCategoryList from './categoryList.json';
import mockTemplate from './template.json';
import mockTemplateList from './templateList.json';

export const handlers = [
  http.get(`${TEMPLATE_API_URL}`, (req) => {
    const url = new URL(req.request.url);
    const categoryId = url.searchParams.get('category');
    const tagId = url.searchParams.get('tag');

    let filteredTemplates = mockTemplateList.templates;

    if (categoryId) {
      filteredTemplates = filteredTemplates.filter((template) => template.category.id.toString() === categoryId);
    }

    if (tagId) {
      filteredTemplates = filteredTemplates.filter((template) =>
        template.tags.some((tag) => tag.id.toString() === tagId),
      );
    }

    return HttpResponse.json({ templates: filteredTemplates });
  }),
  http.get(`${TEMPLATE_API_URL}/:id`, () => HttpResponse.json(mockTemplate)),
  http.post(`${TEMPLATE_API_URL}`, async () => HttpResponse.json({ status: 201 })),
  http.post(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 200 })),
  http.delete(`${TEMPLATE_API_URL}/:id`, async () => HttpResponse.json({ status: 204 })),
  http.get(`${CATEGORY_API_URL}`, () => HttpResponse.json(mockCategoryList)),
];
