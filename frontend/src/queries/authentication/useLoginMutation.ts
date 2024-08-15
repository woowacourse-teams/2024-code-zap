import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';

import { postLogin } from '@/api/authentication';
import { ToastContext } from '@/contexts';
import { useAuth } from '@/hooks/authentication/useAuth';
import { useCustomContext } from '@/hooks/utils';
import { LoginRequest } from '@/types';

export const useLoginMutation = () => {
  const { handleLoginState, handleMemberInfo } = useAuth();
  const { failAlert, successAlert } = useCustomContext(ToastContext);
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (loginInfo: LoginRequest) => postLogin(loginInfo),
    onSuccess: (res) => {
      if (res.memberId === undefined) {
        handleLoginState(false);
        handleMemberInfo(res);
        failAlert('로그인에 실패하였습니다.');
      } else {
        localStorage.setItem('username', String(res.username));
        localStorage.setItem('memberId', String(res.memberId));
        handleMemberInfo(res);
        handleLoginState(true);
        navigate('/');
        successAlert('로그인 성공!');
      }
    },
    onError: (error) => {
      console.error(error);
      failAlert('로그인에 실패하였습니다.');
    },
  });
};
