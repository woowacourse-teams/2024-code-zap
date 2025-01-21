import { useMutation } from '@tanstack/react-query';

import { postLogin } from '@/api';
import { ToastContext } from '@/contexts';
import { useCustomContext, useCustomNavigate } from '@/hooks';
import { useAuth } from '@/hooks/authentication/useAuth';
import { END_POINTS } from '@/routes';
import { LoginRequest, MemberInfo } from '@/types';

export const useLoginMutation = () => {
  const { handleLoginState, handleMemberInfo } = useAuth();
  const { failAlert, successAlert } = useCustomContext(ToastContext);
  const navigate = useCustomNavigate();

  return useMutation<Response, Error, LoginRequest>({
    mutationFn: (loginInfo: LoginRequest) => postLogin(loginInfo),
    onSuccess: async (res) => {
      const authorization = res.headers.get('authorization');

      const response = await res.json() as MemberInfo;
      const { memberId, name } = response;

      if (memberId && name) {
        localStorage.setItem('name', String(name));
        localStorage.setItem('memberId', String(memberId));
        localStorage.setItem('authorization', String(authorization));
        handleMemberInfo({ memberId, name });
        handleLoginState(true);
        successAlert('로그인 성공!');
        navigate(END_POINTS.memberTemplates(memberId));
      }
    },
    onError: () => {
      handleLoginState(false);
      handleMemberInfo({ memberId: undefined, name: undefined });
      failAlert('로그인에 실패하였습니다.');
    },
  });
};
