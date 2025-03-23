/* eslint-disable import/no-named-as-default-member */
/* eslint-disable import/no-named-as-default */
import i18n from 'i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import { initReactI18next } from 'react-i18next';

import landingEn from './en/LandingPage.json';
import landingKo from './ko/LandingPage.json';

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    fallbackLng: 'ko',
    lng: 'ko',
    ns: ['LandingPage'],
    resources: {
      ko: {
        LandingPage: landingKo,
      },
      en: {
        LandingPage: landingEn,
      },
    },
    interpolation: {
      escapeValue: false,
    },
  });
