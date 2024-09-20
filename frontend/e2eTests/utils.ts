import { Page } from '@playwright/test';

interface LoginToCodezapProps {
  page: Page;
  id: string;
  password: string;
}

export const loginToCodezap = async ({ page, id, password }: LoginToCodezapProps) => {
  await page.goto('/');
  await page.getByRole('link', { name: '로그인', exact: true }).getByRole('button').click();
  await page
    .locator('div')
    .filter({ hasText: /^아이디 \(닉네임\)$/ })
    .locator('div')
    .click();
  await page.locator('input[type="text"]').fill(id);
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
  await page.waitForResponse((response) => response.url().includes(apiUrl) && response.status() === 200);
};
