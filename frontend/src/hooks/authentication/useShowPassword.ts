import { useState } from 'react';

export const useShowPassword = () => {
  const [showPassword, setShowPassword] = useState(false);

  const handlePasswordToggle = () => {
    setShowPassword((prevShowPassword) => !prevShowPassword);
  };

  return {
    showPassword,
    handlePasswordToggle,
  };
};
