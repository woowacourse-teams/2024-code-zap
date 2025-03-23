/* eslint-disable import/no-named-as-default-member */
/* eslint-disable import/no-named-as-default */
import i18n from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import { initReactI18next } from 'react-i18next';

import headerEn from './en/Header.json';
import landingEn from './en/LandingPage.json';
import headerKo from './ko/Header.json';
import landingKo from './ko/LandingPage.json';

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'ko',
    lng: 'ko',
    ns: ['LandingPage', 'Header'],
    resources: {
      ko: {
        LandingPage: landingKo,
        Header: headerKo,
      },
      en: {
        LandingPage: landingEn,
        Header: headerEn,
      },
    },
    interpolation: {
      escapeValue: false,
    },
  });
