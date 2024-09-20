import test from '@playwright/test';

test('코드잽 서비스에 들어가서 로그인 할 수 있다.', async ({ page }) => {
  const username = process.env.PLAYWRIGHT_TEST_USERNAME || '';
  const password = process.env.PLAYWRIGHT_TEST_PASSWORD || '';

  await page.goto('/');
  await page.getByRole('button', { name: '로그인', exact: true }).click();
  await page.locator('input[type="text"]').fill(username);
  await page.locator('input[type="text"]').press('Tab');
  await page.locator('input[type="password"]').fill(password);
  await page.locator('form').getByRole('button', { name: '로그인' }).click();
});
