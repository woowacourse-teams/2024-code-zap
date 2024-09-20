import { Page } from '@playwright/test';

interface LoginToCodezapProps {
  page: Page;
  username: string;
  password: string;
}

export const loginToCodezap = async ({ page, username, password }: LoginToCodezapProps) => {
  await page.goto('/');
  await page.getByRole('link', { name: '로그인', exact: true }).getByRole('button').click();

  await page.locator('input[type="text"]').fill(username);

  await page.locator('input[type="text"]').press('Tab');
  await page.locator('input[type="password"]').fill(password);
  await page.locator('form').getByRole('button', { name: '로그인' }).click();

  await waitForSuccess({ page, apiUrl: '/login' });
};

interface WaitForSuccessProps {
  page: Page;
  apiUrl: string;
}

export const waitForSuccess = async ({ page, apiUrl }: WaitForSuccessProps) => {
  await page.waitForResponse(
    (response) => response.url().includes(apiUrl) && (response.status() === 200 || response.status() === 201),
  );
};
