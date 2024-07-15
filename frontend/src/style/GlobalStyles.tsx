import { Global } from '@emotion/react';

const GlobalStyles = () => {
  return (
    <Global
      styles={{
        '*': {
          margin: 0,
          padding: 0,
          border: 0,
          font: 'inherit',
          verticalAlign: 'baseline',
          boxSizing: 'border-box',
          fontSize: '62.5%',
        },
        body: {
          lineHeight: 1,
          listStyle: 'none',
          backgroundColor: '#222831',
        },
        a: { textDecoration: 'none', color: 'inherit' },
      }}
    />
  );
};

export default GlobalStyles;
