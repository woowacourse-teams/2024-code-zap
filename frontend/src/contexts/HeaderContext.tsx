import { createContext, useState, PropsWithChildren } from 'react';

type HeaderContextType = {
  headerHeight: number;
  setHeaderHeight: (height: number) => void;
};

export const HeaderContext = createContext<HeaderContextType | undefined>(undefined);

export const HeaderProvider = ({ children }: PropsWithChildren) => {
  const [headerHeight, setHeaderHeight] = useState(0);

  return <HeaderContext.Provider value={{ headerHeight, setHeaderHeight }}>{children}</HeaderContext.Provider>;
};
