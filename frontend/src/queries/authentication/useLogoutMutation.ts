import { useMutation } from '@tanstack/react-query';

import { postLogout } from '@/api/authentication';
import { useAuth } from '@/hooks/authentication';

export const useLogoutMutation = () => {
  const { handleLoginState } = useAuth();

  return useMutation({
    mutationFn: () => postLogout(),
    onSuccess: () => {
      localStorage.removeItem('username');
      localStorage.removeItem('memberId');
      handleLoginState(false);
    },
    onError: () => {
      console.log('mutation');
    },
  });
};
