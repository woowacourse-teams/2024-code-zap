import { useMutation } from '@tanstack/react-query';

import { postLogout } from '@/api/authentication';
import { useAuth } from '@/hooks/authentication/useAuth';

export const useLogoutMutation = () => {
  const { handleLoginState } = useAuth();

  return useMutation({
    mutationFn: () => postLogout(),
    onSuccess: () => {
      handleLoginState(false);
    },
    onError: () => {
      console.log('mutation');
    },
  });
};
