import { ApiError } from './Error/ApiError';
import { getErrorMessage } from './Error/getErrorMessage';
import { HTTP_STATUS } from './Error/statusCode';

type HttpMethod =
  | 'get'
  | 'GET'
  | 'delete'
  | 'DELETE'
  | 'head'
  | 'HEAD'
  | 'options'
  | 'OPTIONS'
  | 'post'
  | 'POST'
  | 'put'
  | 'PUT'
  | 'patch'
  | 'PATCH'
  | 'purge'
  | 'PURGE'
  | 'link'
  | 'LINK'
  | 'unlink'
  | 'UNLINK';

interface RequestParams {
  [key: string]: string | number | boolean;
}

export class ApiClient {
  baseUrl: string;
  headers: HeadersInit;
  credentials: RequestCredentials;

  constructor(baseUrl: string, headers?: HeadersInit, credentials?: RequestCredentials) {
    this.baseUrl = baseUrl;
    this.headers = headers || { 'Content-Type': 'application/json' };
    this.credentials = credentials || 'same-origin';
  }

  async get(endpoint: string, params?: RequestParams): Promise<Response> {
    const url = new URL(`${this.baseUrl}${endpoint}`);

    if (params) {
      Object.entries(params).forEach(([key, value]) => url.searchParams.append(key, String(value)));
    }

    return this.customFetch('GET', url.toString());
  }

  async post<T>(endpoint: string, body: T): Promise<Response> {
    return this.customFetch('POST', `${this.baseUrl}${endpoint}`, body);
  }

  async put<T>(endpoint: string, body: T): Promise<Response> {
    return this.customFetch('PUT', `${this.baseUrl}${endpoint}`, body);
  }

  async delete(endpoint: string): Promise<Response> {
    return this.customFetch('DELETE', `${this.baseUrl}${endpoint}`);
  }

  private async customFetch(method: HttpMethod, url: string, body?: unknown) {
    try {
      const response = await fetch(url, {
        method,
        headers: this.headers,
        credentials: this.credentials,
        body: body ? JSON.stringify(body) : null,
      });

      if (!response.ok) {
        await this.handleError(response);
      }

      return response;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      } else {
        throw new ApiError('일시적인 네트워크 장애입니다.', 500, 2000, 'fetch 네트워크 에러입니다.');
      }
    }
  }

  private async handleError(response: Response) {
    const { errorCode, instance, detail } = await response.json();

    if (response.status === HTTP_STATUS.UNAUTHORIZED) {
      localStorage.removeItem('name');
      localStorage.removeItem('memberId');
    }

    throw new ApiError(getErrorMessage(errorCode, instance), response.status, errorCode, detail);
  }
}
