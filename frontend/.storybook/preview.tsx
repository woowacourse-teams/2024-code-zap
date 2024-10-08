import type { Preview } from '@storybook/react';
import React from 'react';
import { MemoryRouter } from 'react-router-dom';
import GlobalStyles from '../src/style/GlobalStyles';

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
        <MemoryRouter>
          <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
            <div style={{ border: '2px solid black', borderRadius: '8px', padding: '2rem', background: 'white' }}>
              <Story />
            </div>
            <div style={{ border: '2px solid black', borderRadius: '8px', padding: '2rem', background: 'black' }}>
              <Story />
            </div>
          </div>
        </MemoryRouter>
      </>
    ),
  ],
};

export default preview;
