import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';

import { postLogin } from '@/api/authentication';
import { ToastContext } from '@/context/ToastContext';
import { useAuth } from '@/hooks/authentication/useAuth';
import useCustomContext from '@/hooks/utils/useCustomContext';
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
        successAlert('로그인에 실패하였습니다.');
      } else {
        handleLoginState(true);
        handleMemberInfo(res);
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
