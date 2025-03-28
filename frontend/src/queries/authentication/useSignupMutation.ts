import { useMutation } from '@tanstack/react-query';
import { useTranslation } from 'react-i18next';

import { postSignup } from '@/api';
import { ToastContext } from '@/contexts';
import { useCustomContext, useCustomNavigate } from '@/hooks';
import { END_POINTS } from '@/routes';
import { SignupRequest } from '@/types';

export const useSignupMutation = () => {
  const { failAlert, successAlert } = useCustomContext(ToastContext);
  const { t } = useTranslation();
  const navigate = useCustomNavigate();

  return useMutation({
    mutationFn: (signupInfo: SignupRequest) => postSignup(signupInfo),
    onSuccess: () => {
      navigate(END_POINTS.LOGIN);
      successAlert(t('SignupPage:alert.success'));
    },
    onError: (error) => {
      console.error(error);
      failAlert(t('SignupPage:alert.fail'));
    },
  });
};
