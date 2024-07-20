import { beforeAll, afterAll, afterEach } from '@jest/globals';
import '@testing-library/jest-dom'; // did'nt install this package yet
import { server } from './src/mocks/server';

beforeAll(() => server.listen({ onUnhandledRequest: 'error' }));
afterEach(() => server.resetHandlers());
afterAll(() => server.close());
