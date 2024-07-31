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
        <div style={{ display: 'flex', gap: '1.6rem', flexWrap: 'wrap' }}>
          <div style={{ border: '0.2rem solid black', borderRadius: '8px', padding: '3.2rem', background: '#eeeeee' }}>
            <Story />
          </div>
          <div style={{ border: '0.2rem solid white', borderRadius: '8px', padding: '3.2rem', background: '#222831' }}>
            <Story />
          </div>
        </div>
      </>
    ),
  ],
};

export default preview;
