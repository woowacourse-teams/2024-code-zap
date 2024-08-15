import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';

import { postLogout } from '@/api/authentication';

export const useLogoutMutation = () => {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: () => postLogout(),
    onSuccess: () => {
      navigate('/login');
      localStorage.removeItem('username');
      localStorage.removeItem('memberId');
    },
    onError: () => {
      console.log('mutation');
    },
  });
};
