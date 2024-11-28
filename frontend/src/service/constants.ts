export const VISIBILITY_PUBLIC = 'PUBLIC';
export const VISIBILITY_PRIVATE = 'PRIVATE';
export const DEFAULT_TEMPLATE_VISIBILITY = VISIBILITY_PUBLIC;
export const TEMPLATE_VISIBILITY = [VISIBILITY_PUBLIC, VISIBILITY_PRIVATE] as const;

export const convertToKorVisibility = {
  PUBLIC: '전체 공개',
  PRIVATE: '비공개',
};
