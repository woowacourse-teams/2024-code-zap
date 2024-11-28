import { HttpResponse } from 'msw';

import { ErrorBody } from '@/types';

interface Props<T> {
  status: number;
  body?: T;
  errorBody?: ErrorBody;
  headers?: HeadersInit;
}

export const mockResponse = <T>({ status, body, errorBody, headers }: Props<T>) => {
  if (status >= 200 && status <= 299) {
    return HttpResponse.json({ ...body }, { status, headers });
  }

  if (status >= 400 && status <= 499) {
    return HttpResponse.json(
      {
        ...errorBody,
      },
      { status },
    );
  }

  if (status >= 500 && status <= 599) {
    return HttpResponse.json(
      {
        errorBody,
      },
      { status },
    );
  }

  return HttpResponse.json({ body }, { status });
};
