import { createContext, useState, ReactNode } from 'react';

import { MemberInfo } from '@/types/authentication';

interface AuthContextProps {
  isLogin: boolean;
  memberInfo: MemberInfo;
  handleLoginState: (state: boolean) => void;
  handleMemberInfo: (newMemberInfo: MemberInfo) => void;
}

export const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isLogin, setIsLogin] = useState(false);
  const [memberInfo, setMemberInfo] = useState<MemberInfo>({
    memberId: undefined,
    username: undefined,
  });

  const handleLoginState = (state: boolean) => {
    setIsLogin(state);
  };

  const handleMemberInfo = (newMemberInfo: MemberInfo) => {
    setMemberInfo(newMemberInfo);

    console.log('newMemberInfo', newMemberInfo);
  };

  return (
    <AuthContext.Provider value={{ isLogin, memberInfo, handleLoginState, handleMemberInfo }}>
      {children}
    </AuthContext.Provider>
  );
};
