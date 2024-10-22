import { DEFAULT_SORTING_OPTION, SORTING_OPTIONS } from '@/api';

export const getSortingOptionByValue = (value: string) =>
  SORTING_OPTIONS.find((el) => el.value === value) || DEFAULT_SORTING_OPTION;
