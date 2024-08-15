import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';

import { postLogout } from '@/api/authentication';
import { useAuth } from '@/hooks/authentication';

export const useLogoutMutation = () => {
  const navigate = useNavigate();
  const { handleLoginState } = useAuth();

  return useMutation({
    mutationFn: () => postLogout(),
    onSuccess: () => {
      localStorage.removeItem('username');
      localStorage.removeItem('memberId');
      handleLoginState(false);
      navigate('/login');
    },
    onError: () => {
      console.log('mutation');
    },
  });
};
