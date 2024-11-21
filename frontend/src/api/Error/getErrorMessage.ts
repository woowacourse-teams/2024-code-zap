import { END_POINTS } from '@/routes';

// TODO: errorCode magic number
export const getErrorMessage = (errorCode: number, instance: string) => {
  if (errorCode === 1202 && instance === END_POINTS.CHECK_NAME) {
    return '중복된 아이디입니다';
  }

  return 'APIError 입니다';
};
