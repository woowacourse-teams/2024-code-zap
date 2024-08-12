import { CustomError } from '@/types';

interface Props {
  url: string;
  method?: 'GET' | 'POST' | 'PATCH' | 'DELETE';
  headers?: RequestInit['headers'];
  body?: RequestInit['body'];
  errorMessage?: string;
}

const isCustomError = (response: unknown): response is CustomError =>
  typeof response === 'object' && response !== null && 'status' in response && 'detail' in response;

export const customFetch = async <T>({ url, headers, method = 'GET', body }: Props): Promise<T | CustomError> => {
  try {
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json',
        ...headers,
      },
      credentials: 'include',
      body,
    });

    let responseBody;
    const contentType = response.headers.get('Content-Type');

    if (contentType && contentType.includes('application/json')) {
      responseBody = await response.json();
    } else {
      responseBody = null;
    }

    if (!response.ok) {
      const error: CustomError = {
        type: responseBody.type || 'UnknownError',
        title: responseBody.title || 'Error',
        status: response.status,
        detail: responseBody.detail || 'An error occurred',
        instance: url,
      };

      return error;
    }

    return responseBody as T;
  } catch (error) {
    if (isCustomError(error)) {
      throw error as CustomError;
    } else {
      const networkError: CustomError = {
        type: 'NetworkError',
        title: 'Network Error',
        status: 0,
        detail: error instanceof Error ? error.message : 'An unknown network error occurred',
        instance: url,
      };

      throw networkError;
    }
  }
};
