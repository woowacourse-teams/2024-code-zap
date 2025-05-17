import { useMutation } from '@tanstack/react-query';

import { postLogout } from '@/api';
import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks';
import { useAuth } from '@/hooks/authentication';

export const useLogoutMutation = () => {
  const { handleLoginState } = useAuth();
  const { successAlert } = useCustomContext(ToastContext);

  return useMutation({
    mutationFn: () => postLogout(),
    onSuccess: () => {
      localStorage.removeItem('name');
      localStorage.removeItem('memberId');
      localStorage.removeItem('authorization');
      handleLoginState(false);
      successAlert('로그아웃 성공!');
    },
    onError: (error) => {
      console.error(error);
    },
  });
};
