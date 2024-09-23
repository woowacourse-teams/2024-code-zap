export const END_POINTS = {
  HOME: '/',

  // templates
  MY_TEMPLATES: '/my-templates',
  TEMPLATES_EXPLORE: '/templates',
  TEMPLATE: '/templates/:id',
  TEMPLATES_UPLOAD: '/templates/upload',

  // like
  LIKES: 'like',

  // categories
  CATEGORIES: '/categories',

  // tags
  TAGS: '/tags',

  // authentication
  SIGNUP: '/signup',
  LOGIN: '/login',
  LOGOUT: '/logout',
  LOGIN_CHECK: '/login/check',
  CHECK_NAME: '/check-name',
} as const;
