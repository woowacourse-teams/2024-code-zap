export const END_POINTS = {
  HOME: '/',

  // member
  MEMBER_TEMPLATES: '/members/:memberId/templates',
  memberTemplates: (memberId: number) => `/members/${memberId}/templates`,

  // templates
  TEMPLATES_EXPLORE: '/templates',
  TEMPLATE: '/templates/:id',
  TEMPLATES_UPLOAD: '/templates/upload',
  template: (templateId: number) => `/templates/${templateId}`,

  // like
  LIKES: '/like',

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
