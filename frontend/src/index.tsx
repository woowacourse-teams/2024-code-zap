import { ThemeProvider } from '@emotion/react';
import * as Sentry from '@sentry/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';

import { ScreenReaderOnly } from '@/components';
import { AuthProvider, HeaderProvider, ToastProvider } from '@/contexts';
import { router } from '@/routes';
import { AmplitudeInitializer } from '@/service/amplitude';
import GlobalStyles from '@/style/GlobalStyles';
import { theme } from '@/style/theme';

const queryClient = new QueryClient();

Sentry.init({
  dsn: process.env.SENTRY_DSN,
  environment: process.env.NODE_ENV,
  integrations: [Sentry.browserTracingIntegration(), Sentry.replayIntegration()],
  // Performance Monitoring
  tracesSampleRate: 1.0, //  Capture 100% of the transactions
  // Set 'tracePropagationTargets' to control for which URLs distributed tracing should be enabled
  tracePropagationTargets: ['localhost', `${process.env.REACT_APP_API_URL}`],
  // Session Replay
  replaysSessionSampleRate: 0.1, // This sets the sample rate at 10%. You may want to change it to 100% while in development and then sample at a lower rate in production.
  replaysOnErrorSampleRate: 1.0, // If you're not already sampling the entire session, change the sample rate to 100% when sampling sessions where errors occur.
  enabled: process.env.NODE_ENV !== 'development',
});

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

const enableMocking = async () => {
  if (process.env.NODE_ENV !== 'development') {
    return;
  }

  // const { worker } = await import('./mocks/browser');

  // await worker.start();
};

enableMocking().then(() => {
  root.render(
    <React.StrictMode>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider theme={theme}>
          <AuthProvider>
            <AmplitudeInitializer>
              <ToastProvider>
                <HeaderProvider>
                  <GlobalStyles />
                  <RouterProvider router={router} />
                  <ScreenReaderOnly />
                </HeaderProvider>
              </ToastProvider>
            </AmplitudeInitializer>
          </AuthProvider>
        </ThemeProvider>
      </QueryClientProvider>
    </React.StrictMode>,
  );
});
