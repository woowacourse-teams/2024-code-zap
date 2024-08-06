import { createContext, useState, ReactNode } from 'react';

interface AuthContextProps {
  isLogin: boolean;
  handleLoginState: (state: boolean) => void;
}

export const AuthContext = createContext<AuthContextProps | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [isLogin, setIsLogin] = useState(false);

  const handleLoginState = (state: boolean) => {
    setIsLogin(state);
  };

  return <AuthContext.Provider value={{ isLogin, handleLoginState }}>{children}</AuthContext.Provider>;
};
