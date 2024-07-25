import GlobalStyles from './style/GlobalStyles';
import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import router from './routes/router';

const queryClient = new QueryClient();

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <QueryClientProvider client={ queryClient }>
      <GlobalStyles />
      <RouterProvider router={ router } />
    </QueryClientProvider>
  </React.StrictMode>
);
