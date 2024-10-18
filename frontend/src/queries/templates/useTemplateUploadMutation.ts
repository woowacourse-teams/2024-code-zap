import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';

import { QUERY_KEY, postTemplate } from '@/api';
import { ApiError } from '@/api/Error/ApiError';
import { HTTP_STATUS } from '@/api/Error/statusCode';
import { useToast } from '@/hooks/useToast';

export const useTemplateUploadMutation = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const { failAlert } = useToast();

  return useMutation({
    mutationFn: postTemplate,
    onSuccess: (res) => {
      const location = res.headers.get('location');

      if (location) {
        navigate(location);
      }

      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.TEMPLATE_LIST] });
    },
    onError: (error) => {
      const apiError = error as ApiError;

      // TODO: 해당 에러들을 상위에서 일괄 처리에 대한 고민, 인증 에러에 대한 처리
      if (apiError?.statusCode === HTTP_STATUS.BAD_REQUEST || apiError?.statusCode === HTTP_STATUS.NOT_FOUND) {
        failAlert('템플릿 생성에 실패했습니다. 다시 한 번 시도해주세요');

        return;
      }

      throw error;
    },
  });
};
