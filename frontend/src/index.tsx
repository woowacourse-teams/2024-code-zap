import { ThemeProvider } from '@emotion/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';

import { HeaderProvider } from './context/HeaderContext';
import { ToastProvider } from './context/ToastContext';
import router from './routes/router';
import GlobalStyles from './style/GlobalStyles';
import { theme } from './style/theme';

const queryClient = new QueryClient();

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

const enableMocking = async () => {
  if (process.env.NODE_ENV !== 'development') {
    return;
  }

  const { worker } = await import('./mocks/browser');

  await worker.start();
};

enableMocking().then(() => {
  root.render(
    <React.StrictMode>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider theme={theme}>
          <ToastProvider>
            <HeaderProvider>
              <GlobalStyles />
              <RouterProvider router={router} />
            </HeaderProvider>
          </ToastProvider>
        </ThemeProvider>
      </QueryClientProvider>
    </React.StrictMode>,
  );
});
