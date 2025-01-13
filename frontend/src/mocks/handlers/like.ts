import { http } from 'msw';

import { API_URL } from '@/api';
import mockTemplateList from '@/mocks/fixtures/templateList.json';
import { END_POINTS } from '@/routes';
import { mockResponse } from '@/utils/mockResponse';

export const likeHandlers = [
  http.get(`${API_URL}${END_POINTS.LIKED_TEMPLATES}`, () => {
    const template = mockTemplateList.templates.filter((temp) => temp.isLiked === true);

    return mockResponse({
      status: 200, 
      body: {
        paginationSizes: 1,
        templates: template
      }
    })
  }),
  
  http.post(`${API_URL}${END_POINTS.LIKES}/:templateId`, (req) => {
    const { templateId } = req.params;
    const template = mockTemplateList.templates.find((temp) => temp.id.toString() === templateId);

    if (!template) {
      return mockResponse({
        status: 404,
        body: {
          message: 'Template not found',
        },
      });
    }

    if (template.isLiked) {
      return mockResponse({
        status: 400,
        body: {
          message: 'Already liked',
        },
      });
    }

    template.isLiked = true;
    template.likesCount += 1;

    return mockResponse({
      status: 200,
      body: {
        message: 'Liked successfully',
        likesCount: template.likesCount,
        isLiked: template.isLiked,
      },
    });
  }),

  http.delete(`${API_URL}${END_POINTS.LIKES}/:templateId`, (req) => {
    const { templateId } = req.params;
    const template = mockTemplateList.templates.find((temp) => temp.id.toString() === templateId);

    if (!template) {
      return mockResponse({
        status: 404,
        body: {
          message: 'Template not found',
        },
      });
    }

    if (!template.isLiked) {
      return mockResponse({
        status: 400,
        body: {
          message: 'Not liked yet',
        },
      });
    }

    template.isLiked = false;
    template.likesCount -= 1;

    return mockResponse({
      status: 200,
      body: {
        message: 'Disliked successfully',
        likesCount: template.likesCount,
        isLiked: template.isLiked,
      },
    });
  }),
];
