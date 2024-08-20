import { useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { ToastContext } from '@/contexts';
import { useLoginStateQuery } from '@/queries/authentication';
import { useCustomContext } from '../utils';
import { useAuth } from './useAuth';

export const useCheckLoginState = () => {
  const { error, isError, status } = useLoginStateQuery();
  const navigate = useNavigate();
  const { handleLoginState } = useAuth();
  const { infoAlert } = useCustomContext(ToastContext);

  const handleLoginNavigate = useCallback(() => {
    navigate('/login');
  }, [navigate]);

  useEffect(() => {
    if (isError) {
      handleLoginState(false);
    }

    if (status === 'success') {
      handleLoginState(true);
    }
  }, [error, isError, status, handleLoginNavigate, handleLoginState, infoAlert]);
};
