import { authenticationHandler } from '@/mocks/handlers/authentication';
import { categoryHandlers } from '@/mocks/handlers/category';
import { likeHandlers } from '@/mocks/handlers/like';
import { memberHandlers } from '@/mocks/handlers/member';
import { tagHandlers } from '@/mocks/handlers/tag';
import { templateHandlers } from '@/mocks/handlers/template';

export const handlers = [
  ...tagHandlers,
  ...templateHandlers,
  ...categoryHandlers,
  ...authenticationHandler,
  ...likeHandlers,
  ...memberHandlers,
];
