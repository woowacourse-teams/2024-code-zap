import '@emotion/react';

declare module '@emotion/react' {
  export interface Theme {
    mode: 'dark' | 'light';
    color: {
      dark: ColorPalette;
      light: ColorPalette;
    };
    font: {
      size: {
        heading: FontSize;
        text: FontSize;
      };
      weight: FontWeight;
    };
  }
}

type ColorPalette = {
  white: string;
  black: string;
  primary_50: string;
  primary_100: string;
  primary_200: string;
  primary_300: string;
  primary_400: string;
  primary_500: string;
  primary_600: string;
  primary_700: string;
  primary_800: string;
  primary_900: string;
  secondary_50: string;
  secondary_100: string;
  secondary_200: string;
  secondary_300: string;
  secondary_400: string;
  secondary_500: string;
  secondary_600: string;
  secondary_700: string;
  secondary_800: string;
  secondary_900: string;
  tertiary_50: string;
  tertiary_100: string;
  tertiary_200: string;
  tertiary_300: string;
  tertiary_400: string;
  tertiary_500: string;
  tertiary_600: string;
  tertiary_700: string;
  tertiary_800: string;
  tertiary_900: string;
  complementary_50: string;
  complementary_100: string;
  complementary_200: string;
  complementary_300: string;
  complementary_400: string;
  complementary_500: string;
  complementary_600: string;
  complementary_700: string;
  complementary_800: string;
  complementary_900: string;
  analogous_primary_50: string;
  analogous_primary_100: string;
  analogous_primary_200: string;
  analogous_primary_300: string;
  analogous_primary_400: string;
  analogous_primary_500: string;
  analogous_primary_600: string;
  analogous_primary_700: string;
  analogous_primary_800: string;
  analogous_primary_900: string;
  analogous_secondary_50: string;
  analogous_secondary_100: string;
  analogous_secondary_200: string;
  analogous_secondary_300: string;
  analogous_secondary_400: string;
  analogous_secondary_500: string;
  analogous_secondary_600: string;
  analogous_secondary_700: string;
  analogous_secondary_800: string;
  analogous_secondary_900: string;
  triadic_primary_50: string;
  triadic_primary_100: string;
  triadic_primary_200: string;
  triadic_primary_300: string;
  triadic_primary_400: string;
  triadic_primary_500: string;
  triadic_primary_600: string;
  triadic_primary_700: string;
  triadic_primary_800: string;
  triadic_primary_900: string;
  triadic_secondary_50: string;
  triadic_secondary_100: string;
  triadic_secondary_200: string;
  triadic_secondary_300: string;
  triadic_secondary_400: string;
  triadic_secondary_500: string;
  triadic_secondary_600: string;
  triadic_secondary_700: string;
  triadic_secondary_800: string;
  triadic_secondary_900: string;
};

type FontSize = {
  xlarge: string;
  large: string;
  medium: string;
  small: string;
  xsmall: string;
};

type FontWeight = {
  light: number;
  regular: number;
  bold: number;
};
