import { http, HttpResponse } from 'msw';

// const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080';

export const handlers = [
  http.get(`http://localhost:8080/templates`, () => {
    const response = HttpResponse.json({
      data: {
        templates: [{ title: 'apple' }],
      },
    });

    return response;
  }),
];
