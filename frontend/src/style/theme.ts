import { Theme } from '@emotion/react';

import { darkMode, lightMode } from './color';
import { fontSize, fontWeight } from './font';

export const theme: Theme = {
  mode: 'light',
  color: { darkMode, lightMode },
  font: { size: fontSize, weight: fontWeight },
};
