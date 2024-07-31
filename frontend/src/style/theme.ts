import { Theme } from '@emotion/react';

import { dark, light } from './color';
import { fontSize, fontWeight } from './font';

export const theme: Theme = {
  mode: 'light',
  color: { dark, light },
  font: { size: fontSize, weight: fontWeight },
};
