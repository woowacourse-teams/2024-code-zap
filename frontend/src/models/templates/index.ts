export const PAGE_SIZE = 20;

export const SORTING_OPTIONS = [
  {
    key: 'createdAt,desc',
    value: '최근 생성 순',
  },
  {
    key: 'modifiedAt,desc',
    value: '최근 수정 순',
  },
  {
    key: 'createdAt,asc',
    value: '오래된 순',
  },
  {
    key: 'likesCount,desc',
    value: '좋아요 순',
  },
] as const;

export const DEFAULT_SORTING_OPTION = SORTING_OPTIONS[0];
