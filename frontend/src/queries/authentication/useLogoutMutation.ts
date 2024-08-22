import { useMutation } from '@tanstack/react-query';

import { postLogout } from '@/api/authentication';
import { ToastContext } from '@/contexts';
import { useAuth } from '@/hooks/authentication';
import { useCustomContext } from '@/hooks/utils';

export const useLogoutMutation = () => {
  const { handleLoginState } = useAuth();
  const { successAlert } = useCustomContext(ToastContext);

  return useMutation({
    mutationFn: () => postLogout(),
    onSuccess: () => {
      localStorage.removeItem('name');
      localStorage.removeItem('memberId');
      handleLoginState(false);
      successAlert('로그아웃 성공!');
    },
    onError: (error) => {
      console.error(error);
    },
  });
};
