import { useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useLoginStateQuery } from '@/queries/authentication/useLoginStateQuery';
import { useAuth } from './useAuth';

export const useCheckLoginState = () => {
  const { error, isError, isSuccess } = useLoginStateQuery();
  const navigate = useNavigate();
  const { handleLoginState } = useAuth();

  const handleLoginNavigate = useCallback(() => {
    navigate('/login');
  }, [navigate]);

  useEffect(() => {
    if (isError) {
      alert(error.message);
      handleLoginNavigate();
      handleLoginState(false);
    }

    if (isSuccess) {
      handleLoginState(true);
    }
  }, [error, isError, isSuccess, handleLoginNavigate, handleLoginState]);
};
