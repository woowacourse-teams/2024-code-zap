/* eslint-disable import/no-named-as-default-member */
/* eslint-disable import/no-named-as-default */
import i18n from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import { initReactI18next } from 'react-i18next';

import headerEn from './en/Header.json';
import landingEn from './en/LandingPage.json';
import loginPageEn from './en/LoginPage.json';
import memberTemplatePageEn from './en/MemberTemplatePage.json';
import myLikedTemplatePageEn from './en/MyLikedTemplatePage.json';
import signupPageEn from './en/SignupPage.json';
import templateExplorePageEn from './en/TemplateExplorePage.json';
import templateUploadPageEn from './en/TemplateUploadPage.json';
import categoryEn from './en/components/Category.json';
import contactUsEn from './en/components/ContactUs.json';
import modelsTemplatesEn from './en/models/Templates.json';
import constantsEn from './en/service/constants.json';
import hotTopicEn from './en/service/hotTopic.json';
import validatesEn from './en/service/validates.json';
import headerKo from './ko/Header.json';
import landingKo from './ko/LandingPage.json';
import loginPageKo from './ko/LoginPage.json';
import memberTemplatePageKo from './ko/MemberTemplatePage.json';
import myLikedTemplatePageKo from './ko/MyLikedTemplatePage.json';
import signupPageKo from './ko/SignupPage.json';
import templateExplorePageKo from './ko/TemplateExplorePage.json';
import templateUploadPageKo from './ko/TemplateUploadPage.json';
import categoryKo from './ko/components/Category.json';
import contactUsKo from './ko/components/ContactUs.json';
import modelsTemplatesKo from './ko/models/Templates.json';
import constantsKo from './ko/service/constants.json';
import hotTopicKo from './ko/service/hotTopic.json';
import validatesKo from './ko/service/validates.json';

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'en',
    lng: 'en',
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
      },
    },
    interpolation: {
      escapeValue: false,
    },
  });
