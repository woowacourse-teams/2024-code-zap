import { useContext } from 'react';

export const useCustomContext = <T>(context: React.Context<T>) => {
  const value = useContext(context);

  if (!value) {
    throw new Error('컨텍스트가 존재하지 않습니다.');
  }

  return value;
};
