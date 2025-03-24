/* eslint-disable import/no-named-as-default-member */
/* eslint-disable import/no-named-as-default */
import i18n from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import { initReactI18next } from 'react-i18next';

import headerEn from './en/Header.json';
import landingEn from './en/LandingPage.json';
import memberTemplatePageEn from './en/MemberTemplatePage.json';
import headerKo from './ko/Header.json';
import landingKo from './ko/LandingPage.json';
import memberTemplatePageKo from './ko/MemberTemplatePage.json';

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'en',
    lng: 'en',
    ns: ['LandingPage', 'Header', 'MemberTemplatePage'],
    resources: {
      ko: {
        LandingPage: landingKo,
        Header: headerKo,
        MemberTemplatePage: memberTemplatePageKo,
      },
      en: {
        LandingPage: landingEn,
        Header: headerEn,
        MemberTemplatePage: memberTemplatePageEn,
      },
    },
    interpolation: {
      escapeValue: false,
    },
  });
