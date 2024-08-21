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

  ::-webkit-scrollbar-track {
    background-color: ${theme.color.light.secondary_100};
    -webkit-box-shadow: inset 0 0 3px rgba(0, 0, 0, 0.3);
  }

  ::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  ::-webkit-scrollbar-thumb {
    background-color: ${theme.color.light.secondary_400};
  }
`;

const GlobalStyles = () => <Global styles={globalStyles} />;

export default GlobalStyles;
