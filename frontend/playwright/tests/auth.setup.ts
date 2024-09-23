import { test as setup } from '@playwright/test';
import path from 'path';

const authFile = path.join(__dirname, '../.auth/user.json');

setup('authenticate', async ({ page }) => {
  const username = process.env.PLAYWRIGHT_TEST_USERNAME || '';
  const password = process.env.PLAYWRIGHT_TEST_PASSWORD || '';

  await page.goto('/');
  await page.getByRole('button', { name: '로그인', exact: true }).click();
  await page.locator('input[type="text"]').fill(username);
  await page.locator('input[type="text"]').press('Tab');
  await page.locator('input[type="password"]').fill(password);
  await page.locator('form').getByRole('button', { name: '로그인' }).click();

  await page.waitForURL('/my-templates');

  await page.context().storageState({ path: authFile });
});
