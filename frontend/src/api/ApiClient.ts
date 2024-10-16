import { ApiError } from './Error/ApiError';
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

class ApiClient {
  baseUrl: string;
  headers: HeadersInit;
  credentials: RequestCredentials;

  constructor(baseUrl: string, headers?: HeadersInit, credentials?: RequestCredentials) {
    this.baseUrl = baseUrl;
    this.headers = headers || { 'Content-Type': 'application/json' };
    this.credentials = credentials || 'same-origin';
  }

  async get<T>(endpoint: string, params?: RequestParams): Promise<T> {
    const url = new URL(`${this.baseUrl}${endpoint}`);

    if (params) {
      Object.entries(params).forEach(([key, value]) => url.searchParams.append(key, String(value)));
    }

    return this.customFetch<T>('GET', url.toString());
  }

  async post<T, U>(endpoint: string, body: U): Promise<T> {
    return this.customFetch<T>('POST', `${this.baseUrl}${endpoint}`, body);
  }

  async put<T, U>(endpoint: string, body: U): Promise<T> {
    return this.customFetch<T>('PUT', `${this.baseUrl}${endpoint}`, body);
  }

  async delete<T>(endpoint: string): Promise<T> {
    return this.customFetch<T>('DELETE', `${this.baseUrl}${endpoint}`);
  }

  private async customFetch<T>(method: HttpMethod, url: string, body?: unknown) {
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

      if (response.headers.get('content-length') === '0') {
        return null as T;
      }

      return response.json() as Promise<T>;
    } catch (error) {
      if (error instanceof ApiError) {
        throw error;
      } else {
        throw new ApiError('일시적인 네트워크 장애입니다.', 500, 2000, 'fetch 네트워크 에러입니다.');
      }
    }
  }

  private async handleError(response: Response) {
    const errorBody = await response.json();

    if (response.status === HTTP_STATUS.UNAUTHORIZED) {
      localStorage.removeItem('name');
      localStorage.removeItem('memberId');
    }

    // TODO: 에러코드에 따른 메시지 반환 함수 만들기
    throw new ApiError('에러메시지입니다.', response.status, errorBody.errorCode, errorBody.detail);
  }
}

const API_URL = process.env.REACT_APP_API_URL ?? '';

const httpHeader = {
  'Content-Type': 'application/json',
};

const httpCredentials = 'include';

export const apiClient = new ApiClient(API_URL, httpHeader, httpCredentials);
