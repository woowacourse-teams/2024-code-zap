import { AuthContext } from '@/contexts';
import { useCustomContext } from '@/hooks';

export const useAuth = () => useCustomContext(AuthContext);
