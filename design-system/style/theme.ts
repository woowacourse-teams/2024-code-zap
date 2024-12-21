// import { Theme } from '@emotion/react';

import { dark, light } from './color';
import { fontSize, fontWeight } from './font';

import { Theme as EmotionTheme } from '@emotion/react';

interface Theme extends EmotionTheme {
  mode: 'light' | 'dark';
  color: {
    dark: any; // `dark` 색상 정의
    light: any; // `light` 색상 정의
  };
  font: {
    size: any;
    weight: any;
  };
}

export const theme: Theme = {
  mode: 'light',
  color: { dark, light },
  font: { size: fontSize, weight: fontWeight },
};
