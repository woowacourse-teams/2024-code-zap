import { HttpResponse, http } from 'msw';

import { API_URL } from '@/api';
import mockTemplateList from '@/mocks/fixtures/templateList.json';
import { END_POINTS } from '@/routes';

export const likeHandlers = [
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
