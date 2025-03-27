/* eslint-disable import/no-named-as-default-member */
/* eslint-disable import/no-named-as-default */
import i18n from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import { initReactI18next } from 'react-i18next';

import headerEn from './en/Header.json';
import landingEn from './en/LandingPage.json';
import memberTemplatePageEn from './en/MemberTemplatePage.json';
import myLikedTemplatePageEn from './en/MyLikedTemplatePage.json';
import signupPageEn from './en/SignupPage.json';
import templateExplorePageEn from './en/TemplateExplorePage.json';
import templateUploadPageEn from './en/TemplateUploadPage.json';
import categoryEn from './en/components/Category.json';
import contactUsEn from './en/components/ContactUs.json';
import modelsTemplatesEn from './en/models/Templates.json';
import ConstantsEn from './en/service/constants.json';
import HotTopicEn from './en/service/hotTopic.json';
import headerKo from './ko/Header.json';
import landingKo from './ko/LandingPage.json';
import memberTemplatePageKo from './ko/MemberTemplatePage.json';
import myLikedTemplatePageKo from './ko/MyLikedTemplatePage.json';
import signupPageKo from './ko/SignupPage.json';
import templateExplorePageKo from './ko/TemplateExplorePage.json';
import templateUploadPageKo from './ko/TemplateUploadPage.json';
import categoryKo from './ko/components/Category.json';
import contactUsKo from './ko/components/ContactUs.json';
import modelsTemplatesKo from './ko/models/Templates.json';
import ConstantsKo from './ko/service/constants.json';
import HotTopicKo from './ko/service/hotTopic.json';

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'en',
    lng: 'en',
    ns: [
      'Category',
      'ContactUs',
      'SignupPage',
      'LandingPage',
      'Header',
      'MemberTemplatePage',
      'MyLikedTemplatePage',
      'TemplateExplorePage',
      'ModelsTemplates',
      'HotTopic',
      'Constants',
    ],
    resources: {
      ko: {
        Category: categoryKo,
        ContactUs: contactUsKo,
        SignupPage: signupPageKo,
        LandingPage: landingKo,
        Header: headerKo,
        MemberTemplatePage: memberTemplatePageKo,
        MyLikedTemplatePage: myLikedTemplatePageKo,
        TemplateExplorePage: templateExplorePageKo,
        TemplateUploadPage: templateUploadPageKo,
        ModelsTemplates: modelsTemplatesKo,
        HotTopic: HotTopicKo,
        Constants: ConstantsKo,
      },
      en: {
        Category: categoryEn,
        ContactUs: contactUsEn,
        SignupPage: signupPageEn,
        LandingPage: landingEn,
        Header: headerEn,
        MemberTemplatePage: memberTemplatePageEn,
        MyLikedTemplatePage: myLikedTemplatePageEn,
        TemplateExplorePage: templateExplorePageEn,
        TemplateUploadPage: templateUploadPageEn,
        ModelsTemplates: modelsTemplatesEn,
        HotTopic: HotTopicEn,
        Constants: ConstantsEn,
      },
    },
    interpolation: {
      escapeValue: false,
    },
  });
