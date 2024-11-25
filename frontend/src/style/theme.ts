import { Theme } from '@emotion/react';

import { dark, light } from '@/style/color';
import { fontSize, fontWeight } from '@/style/font';

export const theme: Theme = {
  mode: 'light',
  color: { dark, light },
  font: { size: fontSize, weight: fontWeight },
};
