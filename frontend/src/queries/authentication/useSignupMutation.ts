import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';

import { postSignup } from '@/api/authentication';
import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks/utils';
import { END_POINTS } from '@/routes';
import { SignupRequest } from '@/types';

export const useSignupMutation = () => {
  const { failAlert, successAlert } = useCustomContext(ToastContext);
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (signupInfo: SignupRequest) => postSignup(signupInfo),
    onSuccess: () => {
      navigate(END_POINTS.LOGIN);
      successAlert('회원가입 성공!');
    },
    onError: (error) => {
      console.error(error);
      failAlert(error.message);
    },
  });
};
