import type { Preview } from '@storybook/react';
import GlobalStyles from '../src/style/GlobalStyles';
import React from 'react';

const preview: Preview = {
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
    layout: 'centered',
  },
  decorators: [
    (Story) => (
      <>
        <GlobalStyles />
        <div style={{ display: 'flex', gap: '1.6rem' }}>
          <div style={{ border: '0.2rem solid black', borderRadius: '0.8rem', padding: '3.2rem' }}>
            <Story />
          </div>
          <div style={{ border: '0.2rem solid black', borderRadius: '0.8rem', padding: '3.2rem', background: 'black' }}>
            <Story />
          </div>
        </div>
      </>
    ),
  ],
};

export default preview;
