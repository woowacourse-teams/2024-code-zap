import { AuthContext } from '@/contexts';

import { useCustomContext } from '../useCustomContext';

export const useAuth = () => useCustomContext(AuthContext);
