import { useMutation } from '@tanstack/react-query';

import { postLogin } from '@/api/authentication';
import { ToastContext } from '@/contexts';
import { useCustomContext, useCustomNavigate } from '@/hooks';
import { useAuth } from '@/hooks/authentication/useAuth';
import { END_POINTS } from '@/routes';
import { LoginRequest } from '@/types';

export const useLoginMutation = () => {
  const { handleLoginState, handleMemberInfo } = useAuth();
  const { failAlert, successAlert } = useCustomContext(ToastContext);
  const navigate = useCustomNavigate();

  return useMutation({
    mutationFn: (loginInfo: LoginRequest) => postLogin(loginInfo),
    onSuccess: (res) => {
      if (res.memberId === undefined) {
        handleLoginState(false);
        handleMemberInfo(res);
        failAlert('로그인에 실패하였습니다.');
      } else {
        localStorage.setItem('name', String(res.name));
        localStorage.setItem('memberId', String(res.memberId));
        handleMemberInfo(res);
        handleLoginState(true);
        navigate(END_POINTS.memberTemplates(res.memberId));
        successAlert('로그인 성공!');
      }
    },
    onError: (error) => {
      console.error(error);
      failAlert('로그인에 실패하였습니다.');
    },
  });
};
