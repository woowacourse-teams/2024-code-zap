import { HTTP_STATUS } from './statusCode';

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
  errorCode: string;
  detail: string;

  constructor(message: string, statusCode: number, errorCode: string, detail: string) {
    super(message);

    this.statusCode = statusCode;
    this.errorCode = errorCode;
    this.detail = detail;
    this.name = errorNameMap[statusCode] || 'APIError';
  }
}
