import { useInput, useDebounce } from '../';

export const useKeyword = () => {
  const [keyword, handleKeywordChange] = useInput('');
  const debouncedKeyword = useDebounce(keyword, 300);

  return { keyword, debouncedKeyword, handleKeywordChange };
};
