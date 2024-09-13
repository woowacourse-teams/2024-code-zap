import { test } from '@playwright/test';

test('코드잽 서비스에 들어가서 로그인 할 수 있다.', async ({ page }) => {
  await page.goto('https://www.code-zap.com/');
  await page.getByRole('button', { name: '로그인', exact: true }).click();
  await page
    .locator('div')
    .filter({ hasText: /^아이디 \(닉네임\)$/ })
    .locator('div')
    .click();
  await page.locator('input[type="text"]').fill('ll');
  await page.locator('input[type="text"]').press('Tab');
  await page.locator('input[type="password"]').fill('llll1111');
  await page.locator('form').getByRole('button', { name: '로그인' }).click();
});
