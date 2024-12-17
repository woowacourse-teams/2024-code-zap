import type { Preview } from '@storybook/react';

import GlobalStyles from '../style/GlobalStyles';
import { ThemeProvider } from '@emotion/react';
import { theme } from '../style/theme';


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
        // <ThemeProvider theme={theme}>
          // <GlobalStyles />
          <Story />
        // </ThemeProvider>
    ),
  ],
};

export default preview;
