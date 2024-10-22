import { useInput, useDebounce } from '..';

export const useSearchKeyword = (initKeyword: string = '') => {
  const [keyword, handleKeywordChange] = useInput(initKeyword);
  const debouncedKeyword = useDebounce(keyword, 300);

  return { keyword, debouncedKeyword, handleKeywordChange };
};
