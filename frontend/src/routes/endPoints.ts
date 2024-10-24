export const END_POINTS = {
  HOME: '/',

  // member
  MEMBERS: '/members',
  MEMBERS_TEMPLATES: '/members/:memberId/templates',
  memberTemplates: (memberId: number) => `/members/${memberId}/templates`,

  // templates
  TEMPLATES_EXPLORE: '/templates',
  TEMPLATE: '/templates/:id',
  LIKED_TEMPLATES: '/templates/like',
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

export const ROUTE_END_POINT = {
  HOME: '/',

  TEMPLATES_EXPLORE: '/templates',
  TEMPLATE: '/templates/:id',
  template: (templateId: number) => `/templates/${templateId}`,
  TEMPLATES_UPLOAD: '/templates/upload',

  MEMBERS_TEMPLATES: '/members/:memberId/templates',
  memberTemplates: (memberId: number) => `/members/${memberId}/templates`,
  MEMBERS_LIKED_TEMPLATES: '/members/:memberId/liked',
  memberLikedTemplates: (memberId: number) => `/members/${memberId}/liked`,

  SIGNUP: '/signup',
  LOGIN: '/login',
  LOGOUT: '/logout',
} as const;
