import { css, Global } from '@emotion/react';

import { theme } from './theme';

const globalStyles = css`
  @font-face {
    font-family: 'Pretendard';
    font-weight: ${theme.font.weight.light};
    font-style: normal;
    src: url('/fonts/Pretendard-ExtraLight.woff2') format('woff2');
  }

  @font-face {
    font-family: 'Pretendard';
    font-weight: ${theme.font.weight.regular};
    font-style: normal;
    src: url('/fonts/Pretendard-Regular.woff2') format('woff2');
  }

  @font-face {
    font-family: 'Pretendard';
    font-weight: ${theme.font.weight.bold};
    font-style: normal;
    src: url('/fonts/Pretendard-Bold.woff2') format('woff2');
  }

  * {
    box-sizing: border-box;
    margin: 0;
    padding: 0;

    font: inherit;
    vertical-align: baseline;

    border: 0;
  }

  body {
    font-family: 'Pretendard', sans-serif;
    font-size: 1rem;
    line-height: '100%';
    color: ${theme.color.dark.secondary_800};
    list-style: none;

    background-color: ${theme.mode === 'dark' ? theme.color.dark.tertiary_900 : theme.color.light.white};
  }

  a {
    color: inherit;
    text-decoration: none;
  }
`;

const GlobalStyles = () => <Global styles={globalStyles} />;

export default GlobalStyles;
