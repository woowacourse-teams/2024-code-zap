import { createContext, useState, ReactNode, useEffect } from 'react';

import { useLoginStateQuery } from '@/queries/authentication';
import { MemberInfo } from '@/types';

interface AuthContextProps {
  isLogin: boolean;
  isChecking: boolean;
  memberInfo: MemberInfo;
  handleLoginState: (state: boolean) => void;
  handleMemberInfo: (newMemberInfo: MemberInfo) => void;
}

export const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isLogin, setIsLogin] = useState(false);
  const [isChecking, setIsChecking] = useState(true);
  const [memberInfo, setMemberInfo] = useState<MemberInfo>({
    memberId: undefined,
    name: undefined,
  });

  const { refetch: getLoginState } = useLoginStateQuery();

  useEffect(() => {
    const savedName = localStorage.getItem('name');
    const savedMemberId = localStorage.getItem('memberId');

    if (savedName && savedMemberId) {
      setMemberInfo({
        name: savedName,
        memberId: Number(savedMemberId),
      });
    }

    checkAlreadyLogin();
  }, []);

  const checkAlreadyLogin = async () => {
    const { error, isSuccess: refetchSuccess } = await getLoginState();

    const savedName = localStorage.getItem('name');
    const savedMemberId = localStorage.getItem('memberId');

    if (savedName === undefined || savedMemberId === undefined) {
      setIsLogin(false);
    }

    if (refetchSuccess && savedName && savedName) {
      setIsLogin(true);
    }

    if ((error && savedName === undefined) || savedMemberId === undefined) {
      localStorage.removeItem('name');
      localStorage.removeItem('memberId');
      setIsLogin(false);
    }

    setIsChecking(false);
  };

  const handleLoginState = (state: boolean) => {
    setIsLogin(state);
  };

  const handleMemberInfo = (newMemberInfo: MemberInfo) => {
    setMemberInfo(newMemberInfo);
  };

  return (
    <AuthContext.Provider value={{ isLogin, isChecking, memberInfo, handleLoginState, handleMemberInfo }}>
      {children}
    </AuthContext.Provider>
  );
};
