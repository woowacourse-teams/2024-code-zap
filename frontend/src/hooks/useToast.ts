import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks';

export const useToast = () => useCustomContext(ToastContext);
