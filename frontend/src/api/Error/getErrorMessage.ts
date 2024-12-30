import { END_POINTS } from '@/routes';

import { ERROR_CODE } from './errorCode';

// TODO: errorCode magic number
export const getErrorMessage = (errorCode: number, instance: string) => {
  if (errorCode === ERROR_CODE.NOT_FOUND_RESOURCE && instance === END_POINTS.CHECK_NAME) {
    return '중복된 아이디입니다';
  }

  if (errorCode === ERROR_CODE.UNAUTHORIZED_UNEXIST_ID && instance === END_POINTS.LOGIN) {
    return '존재하지 않는 아이디 입니다.';
  }

  if (errorCode === ERROR_CODE.BAD_REQUEST_WRONG_PASSWORD_REQUEST && instance === END_POINTS.LOGIN) {
    return '비밀번호를 확인해주세요.';
  }

  return 'APIError 입니다';
};
