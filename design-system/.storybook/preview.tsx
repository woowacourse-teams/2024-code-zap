import React from 'react';
import { ThemeProvider } from '@emotion/react';
import type { Preview } from '@storybook/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import GlobalStyles from '../style/GlobalStyles';
import { theme } from '../style/theme';
import { AuthProvider, ToastProvider } from '../../frontend/src/contexts';
import { MemoryRouter } from 'react-router-dom';

const queryClient = new QueryClient();

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
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <ToastProvider>
            <ThemeProvider theme={theme}>
              <GlobalStyles />
              <MemoryRouter>
                <Story />
              </MemoryRouter>
            </ThemeProvider>
          </ToastProvider>
        </AuthProvider>
      </QueryClientProvider>
    ),
  ],
};

export default preview;
