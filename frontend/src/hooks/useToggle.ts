import { useState } from 'react';

export const useToggle = (initState: boolean = false) => {
  const [state, setState] = useState(initState);

  const toggleState = () => {
    setState((prev) => !prev);
  };

  return [state, toggleState] as const;
};
