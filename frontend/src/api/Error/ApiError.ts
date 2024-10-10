import { HTTP_STATUS } from './statusCode';

/**
 * statusCode: response의 status
 * errorCode: 예측된 에러의 식별자
 * detail: backend에서 보내는 에러 메시지
 */
export class ApiError extends Error {
  statusCode: number;
  errorCode: string;
  detail: string;

  constructor(message: string, statusCode: number, errorCode: string, detail: string) {
    super(message);

    this.statusCode = statusCode;
    this.errorCode = errorCode;
    this.detail = detail;

    switch (statusCode) {
      case HTTP_STATUS.BAD_REQUEST:
        this.name = 'APIBadRequestError';
        break;

      case HTTP_STATUS.UNAUTHORIZED:
        this.name = 'APIAuthenticationError';
        break;

      case HTTP_STATUS.FORBIDDEN:
        this.name = 'APIForbiddenError';
        break;

      case HTTP_STATUS.NOT_FOUND:
        this.name = 'APINotFoundError';
        break;

      case HTTP_STATUS.INTERNAL_SERVER_ERROR:
        this.name = 'APIInternalServerError';
        break;

      default:
        this.name = 'APIError';
    }
  }
}
