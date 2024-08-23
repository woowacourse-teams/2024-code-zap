import { useContext } from 'react';

import { HeaderContext } from '@/contexts';

export const useHeaderHeight = () => {
  const context = useContext(HeaderContext);

  if (context === undefined) {
    throw new Error('useHeaderHeight must be used within a HeaderProvider');
  }

  return context;
};
