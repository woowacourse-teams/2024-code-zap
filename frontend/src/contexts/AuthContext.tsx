import { createContext, useState, ReactNode, useEffect } from 'react';

import { useLoginStateQuery } from '@/queries/authentication';
import { MemberInfo } from '@/types/authentication';

interface AuthContextProps {
  isLogin: boolean;
  memberInfo: MemberInfo;
  handleLoginState: (state: boolean) => void;
  handleMemberInfo: (newMemberInfo: MemberInfo) => void;
  checkAlreadyLogin: () => Promise<boolean>;
}

export const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isLogin, setIsLogin] = useState(false);
  const [memberInfo, setMemberInfo] = useState<MemberInfo>({
    memberId: undefined,
    username: undefined,
  });

  const { refetch: getLoginState } = useLoginStateQuery();

  useEffect(() => {
    const savedUsername = localStorage.getItem('username');
    const savedMemberId = localStorage.getItem('memberId');

    if (savedUsername && savedMemberId) {
      setMemberInfo({
        username: savedUsername,
        memberId: Number(savedMemberId),
      });
    }
  }, []);

  const checkAlreadyLogin = async () => {
    const { error, isSuccess: refetchSuccess } = await getLoginState();

    const savedUsername = localStorage.getItem('username');
    const savedMemberId = localStorage.getItem('memberId');

    if (savedUsername === undefined || savedMemberId === undefined) {
      setIsLogin(false);

      return false;
    }

    if (refetchSuccess && savedUsername && savedUsername) {
      setIsLogin(true);

      return true;
    }

    if ((error && savedUsername === undefined) || savedMemberId === undefined) {
      localStorage.removeItem('username');
      localStorage.removeItem('memberId');
      setIsLogin(false);

      return false;
    }

    return false;
  };

  const handleLoginState = (state: boolean) => {
    setIsLogin(state);
  };

  const handleMemberInfo = (newMemberInfo: MemberInfo) => {
    setMemberInfo(newMemberInfo);
  };

  return (
    <AuthContext.Provider value={{ isLogin, memberInfo, handleLoginState, handleMemberInfo, checkAlreadyLogin }}>
      {children}
    </AuthContext.Provider>
  );
};
