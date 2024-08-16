import { css, Global } from '@emotion/react';

import { theme } from './theme';

const globalStyles = css`
  * {
    box-sizing: border-box;
    margin: 0;
    padding: 0;

    font: inherit;
    vertical-align: baseline;
    list-style: none;

    border: 0;
  }

  body {
    font-family: 'Pretendard', sans-serif;
    line-height: 100%;
    color: ${theme.color.dark.secondary_800};
    background-color: ${theme.mode === 'dark' ? theme.color.dark.tertiary_900 : theme.color.light.white};
  }

  a {
    color: inherit;
    text-decoration: none;
  }

  button {
    padding: 0;

    background: inherit;
    border: none;
    border-radius: 0;
    box-shadow: none;
  }
`;

const GlobalStyles = () => <Global styles={globalStyles} />;

export default GlobalStyles;
