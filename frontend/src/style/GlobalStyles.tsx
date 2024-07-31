import { Global } from '@emotion/react';

const GlobalStyles = () => (
  <Global
    styles={{
      '*': {
        margin: 0,
        padding: 0,
        border: 0,
        font: 'inherit',
        verticalAlign: 'baseline',
        boxSizing: 'border-box',
      },
      body: {
        lineHeight: '100%',
        listStyle: 'none',
        backgroundColor: '#222831',
      },
      a: { textDecoration: 'none', color: 'inherit' },
    }}
  />
);

export default GlobalStyles;
