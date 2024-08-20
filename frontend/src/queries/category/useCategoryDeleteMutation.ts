import { useMutation, useQueryClient } from '@tanstack/react-query';

import { deleteCategory } from '@/api/categories';
import { QUERY_KEY } from '@/api/queryKeys';
import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks/utils';
import { Category, CustomError } from '@/types';

export const useCategoryDeleteMutation = (categories: Category[]) => {
  const queryClient = useQueryClient();
  const { failAlert } = useCustomContext(ToastContext);

  return useMutation({
    mutationFn: deleteCategory,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [QUERY_KEY.CATEGORY_LIST] });
    },
    onError: (error: CustomError, targetCategory) => {
      const categoryId = targetCategory.id;
      const categoryName = categories.find((category) => category.id === categoryId)?.name || '카테고리를 찾을 수 없음';

      if (error.status === 400) {
        failAlert(`템플릿이 존재하는 카테고리(${categoryName})는 삭제할 수 없습니다.`);
      } else {
        failAlert(`카테고리 삭제 중 오류가 발생했습니다: ${categoryName}`);
      }
    },
  });
};
