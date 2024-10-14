import { ApiError } from './Error/ApiError';
import { HTTP_STATUS } from './Error/statusCode';

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

    const response = await fetch(url.toString(), {
      method: 'GET',
      headers: this.headers,
      credentials: this.credentials,
    });

    return this.handleResponse<T>(response);
  }

  async post<T, U>(endpoint: string, body: U): Promise<T> {
    const response = await fetch(`${this.baseUrl}${endpoint}`, {
      method: 'POST',
      headers: this.headers,
      credentials: this.credentials,
      body: JSON.stringify(body),
    });

    return this.handleResponse<T>(response);
  }

  async put<T, U>(endpoint: string, body: U): Promise<T> {
    const response = await fetch(`${this.baseUrl}${endpoint}`, {
      method: 'PUT',
      headers: this.headers,
      credentials: this.credentials,
      body: JSON.stringify(body),
    });

    return this.handleResponse<T>(response);
  }

  async delete<T>(endpoint: string): Promise<T> {
    const response = await fetch(`${this.baseUrl}${endpoint}`, {
      method: 'DELETE',
      headers: this.headers,
      credentials: this.credentials,
    });

    return this.handleResponse<T>(response);
  }

  private async handleResponse<T>(response: Response): Promise<T> {
    if (!response.ok) {
      const errorBody = await response.json();

      if (response.status === HTTP_STATUS.UNAUTHORIZED) {
        localStorage.removeItem('name');
        localStorage.removeItem('memberId');
      }

      // TODO: 에러코드에 따른 메시지 반환 함수 만들기
      throw new ApiError('에러메시지입니다.', response.status, errorBody.errorCode, errorBody.detail);
    }

    return response.json() as Promise<T>;
  }
}

const API_URL = process.env.REACT_APP_API_URL ?? '';

const httpHeader = {
  'Content-Type': 'application/json',
};

const httpCredentials = 'include';

export const apiClient = new ApiClient(API_URL, httpHeader, httpCredentials);
