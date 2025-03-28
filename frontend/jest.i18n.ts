/* eslint-disable import/no-named-as-default-member */
/* eslint-disable import/no-named-as-default */
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

import headerEn from './src/i18n/en/Header.json';
import landingEn from './src/i18n/en/LandingPage.json';
import loginPageEn from './src/i18n/en/LoginPage.json';
import memberTemplatePageEn from './src/i18n/en/MemberTemplatePage.json';
import myLikedTemplatePageEn from './src/i18n/en/MyLikedTemplatePage.json';
import notFoundPageEn from './src/i18n/en/NotFoundPage.json';
import signupPageEn from './src/i18n/en/SignupPage.json';
import templateExplorePageEn from './src/i18n/en/TemplateExplorePage.json';
import templateUploadPageEn from './src/i18n/en/TemplateUploadPage.json';
import categoryEn from './src/i18n/en/components/Category.json';
import contactUsEn from './src/i18n/en/components/ContactUs.json';
import modelsTemplatesEn from './src/i18n/en/models/Templates.json';
import constantsEn from './src/i18n/en/service/constants.json';
import hotTopicEn from './src/i18n/en/service/hotTopic.json';
import validatesEn from './src/i18n/en/service/validates.json';
import formatRelativeTimeEn from './src/i18n/en/utils/formatRelativeTime.json';
import headerKo from './src/i18n/ko/Header.json';
import landingKo from './src/i18n/ko/LandingPage.json';
import loginPageKo from './src/i18n/ko/LoginPage.json';
import memberTemplatePageKo from './src/i18n/ko/MemberTemplatePage.json';
import myLikedTemplatePageKo from './src/i18n/ko/MyLikedTemplatePage.json';
import notFoundPageKo from './src/i18n/ko/NotFoundPage.json';
import signupPageKo from './src/i18n/ko/SignupPage.json';
import templateExplorePageKo from './src/i18n/ko/TemplateExplorePage.json';
import templateUploadPageKo from './src/i18n/ko/TemplateUploadPage.json';
import categoryKo from './src/i18n/ko/components/Category.json';
import contactUsKo from './src/i18n/ko/components/ContactUs.json';
import modelsTemplatesKo from './src/i18n/ko/models/Templates.json';
import constantsKo from './src/i18n/ko/service/constants.json';
import hotTopicKo from './src/i18n/ko/service/hotTopic.json';
import validatesKo from './src/i18n/ko/service/validates.json';
import formatRelativeTimeKo from './src/i18n/ko/utils/formatRelativeTime.json';

i18n.use(initReactI18next).init({
  lng: 'ko',
  ns: [
    'Category',
    'ContactUs',
    'LoginPage',
    'SignupPage',
    'LandingPage',
    'Header',
    'MemberTemplatePage',
    'MyLikedTemplatePage',
    'TemplateExplorePage',
    'ModelsTemplates',
    'HotTopic',
    'Constants',
    'Validates',
    'FormatRelativeTime',
    'NotFoundPage',
  ],
  resources: {
    ko: {
      Category: categoryKo,
      ContactUs: contactUsKo,
      LoginPage: loginPageKo,
      SignupPage: signupPageKo,
      LandingPage: landingKo,
      Header: headerKo,
      MemberTemplatePage: memberTemplatePageKo,
      MyLikedTemplatePage: myLikedTemplatePageKo,
      TemplateExplorePage: templateExplorePageKo,
      TemplateUploadPage: templateUploadPageKo,
      ModelsTemplates: modelsTemplatesKo,
      HotTopic: hotTopicKo,
      Constants: constantsKo,
      Validates: validatesKo,
      FormatRelativeTime: formatRelativeTimeKo,
      NotFoundPage: notFoundPageKo,
    },
    en: {
      Category: categoryEn,
      ContactUs: contactUsEn,
      LoginPage: loginPageEn,
      SignupPage: signupPageEn,
      LandingPage: landingEn,
      Header: headerEn,
      MemberTemplatePage: memberTemplatePageEn,
      MyLikedTemplatePage: myLikedTemplatePageEn,
      TemplateExplorePage: templateExplorePageEn,
      TemplateUploadPage: templateUploadPageEn,
      ModelsTemplates: modelsTemplatesEn,
      HotTopic: hotTopicEn,
      Constants: constantsEn,
      Validates: validatesEn,
      FormatRelativeTime: formatRelativeTimeEn,
      NotFoundPage: notFoundPageEn,
    },
  },
  interpolation: {
    escapeValue: false,
  },
});
