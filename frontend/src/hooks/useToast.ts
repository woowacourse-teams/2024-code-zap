import { ToastContext } from '@/contexts';

import { useCustomContext } from './useCustomContext';

export const useToast = () => useCustomContext(ToastContext);
