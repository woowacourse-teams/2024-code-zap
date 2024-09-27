import { Page } from '@playwright/test';

interface WaitForSuccessProps {
  page: Page;
  apiUrl: string;
}

export const waitForSuccess = async ({ page, apiUrl }: WaitForSuccessProps) => {
  await page.waitForResponse(
    (response) => response.url().includes(apiUrl) && (response.status() === 200 || response.status() === 201),
  );
};
