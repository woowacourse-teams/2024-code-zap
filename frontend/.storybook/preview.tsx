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
        <div style={{ display: 'flex', gap: '16px' }}>
          <div style={{ border: '2px solid black', borderRadius: '8px', padding: '32px' }}>
            <Story />
          </div>
          <div style={{ border: '2px solid black', borderRadius: '8px', padding: '32px', background: 'black' }}>
            <Story />
          </div>
        </div>
      </>
    ),
  ],
};

export default preview;
