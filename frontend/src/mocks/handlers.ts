import { http, HttpResponse } from 'msw';
import mockTemplates from './templateList.json';

// change this url after MSW initial setting
// const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const handlers = [
  http.get('http://localhost:8080/templates', () => {
    const response = HttpResponse.json(mockTemplates);

    return response;
  }),
];
