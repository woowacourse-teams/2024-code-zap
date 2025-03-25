export const PAGE_SIZE = 20;

export const SORTING_OPTIONS = [
  {
    key: 'createdAt,desc',
    value: 'createdAtDesc',
  },
  {
    key: 'modifiedAt,desc',
    value: 'modifiedAtDesc',
  },
  {
    key: 'createdAt,asc',
    value: 'createdAtAsc',
  },
  {
    key: 'likesCount,desc',
    value: 'likesCountDesc',
  },
] as const;

export const DEFAULT_SORTING_OPTION = SORTING_OPTIONS[0];
