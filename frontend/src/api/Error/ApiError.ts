import { HTTP_STATUS } from '@/api/Error';

/**
 * statusCode: response의 status
 * errorCode: 예측된 에러의 식별자
 * detail: backend에서 보내는 에러 메시지
 */

const errorNameMap: Record<number, string> = {
  [HTTP_STATUS.BAD_REQUEST]: 'APIBadRequestError',
  [HTTP_STATUS.UNAUTHORIZED]: 'APIAuthenticationError',
  [HTTP_STATUS.FORBIDDEN]: 'APIForbiddenError',
  [HTTP_STATUS.NOT_FOUND]: 'APINotFoundError',
  [HTTP_STATUS.INTERNAL_SERVER_ERROR]: 'APIInternalServerError',
};

export class ApiError extends Error {
  statusCode: number;
  errorCode: number;
  detail: string;

  constructor(message: string, statusCode: number, errorCode: number, detail: string) {
    super(message);

    this.statusCode = statusCode;
    this.errorCode = errorCode || 9999;
    this.detail = detail || '정의되지 않은 detail입니다.';
    this.name = errorNameMap[statusCode] || 'APIError';
  }
}
