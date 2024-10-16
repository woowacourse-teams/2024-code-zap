import { beforeAll, afterAll, afterEach } from '@jest/globals';

import { server } from './src/mocks/server';

beforeAll(() =>
  server.listen({
    onUnhandledRequest(request) {
      console.log('Unhandled %s %s', request.method, request.url);
    },
  }),
);
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
