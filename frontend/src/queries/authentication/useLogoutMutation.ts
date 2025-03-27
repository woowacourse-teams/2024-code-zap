import { useMutation } from '@tanstack/react-query';
import { useTranslation } from 'react-i18next';

import { postLogout } from '@/api';
import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks';
import { useAuth } from '@/hooks/authentication';

export const useLogoutMutation = () => {
  const { handleLoginState } = useAuth();
  const { t } = useTranslation();
  const { successAlert } = useCustomContext(ToastContext);

  return useMutation({
    mutationFn: () => postLogout(),
    onSuccess: () => {
      localStorage.removeItem('name');
      localStorage.removeItem('memberId');
      localStorage.removeItem('authorization');
      handleLoginState(false);
      successAlert(t('LoginPage:alert.logoutSuccess'));
    },
    onError: (error) => {
      console.error(error);
    },
  });
};
