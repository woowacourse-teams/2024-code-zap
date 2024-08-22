import { useMutation } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';

import { ERROR_CODE } from '@/api';
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

        if (res.errorCode === ERROR_CODE.invalidName) {
          failAlert('존재하지 않는 아이디에요. 다시 로그인 해주세요.');

          return;
        }

        if (res.errorCode === ERROR_CODE.invalidPassword) {
          failAlert('잘못된 비밀번호에요. 다시 로그인 해주세요.');

          return;
        }

        failAlert('로그인에 실패했어요. 잠시 후 다시 시도해주세요.');
      } else {
        localStorage.setItem('name', String(res.name));
        localStorage.setItem('memberId', String(res.memberId));
        handleMemberInfo(res);
        handleLoginState(true);
        navigate('/');
        successAlert('로그인에 성공했어요!');
      }
    },
    onError: (error) => {
      console.error(error);
      failAlert('로그인에 실패했어요. 잠시 후 다시 시도해주세요.');
    },
  });
};
