import { useCallback, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useLoginStateQuery } from '@/queries/authentication/useLoginStateQuery';

export const useCheckLoginState = () => {
  const { error, isError } = useLoginStateQuery();
  const navigate = useNavigate();

  const handleLoginNavigate = useCallback(() => {
    navigate('/login');
  }, [navigate]);

  useEffect(() => {
    if (isError) {
      alert(error.message);
      handleLoginNavigate();
    }
  }, [error, isError, handleLoginNavigate]);
};
