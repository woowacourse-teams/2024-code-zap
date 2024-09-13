import { test, expect } from '@playwright/test';

test('코드잽 서비스에 들어가서 로그인 후, 단일 템플릿을 수정한다.', async ({ page }) => {
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
  await page.getByRole('link', { name: '테스트1' }).click();
  await page.getByRole('button', { name: '템플릿 편집' }).click();
  await page.getByPlaceholder('제목을 입력해주세요').click();
  await page.getByPlaceholder('제목을 입력해주세요').fill('테스트2');
  await page.getByRole('button', { name: '저장' }).click();

  await expect(page.getByRole('heading', { name: '테스트2' })).toBeVisible;
});

test('코드잽 서비스에 로그인 후 새로운 템플릿을 업로드 한다', async ({ page }) => {
  await page.goto('https://www.code-zap.com/');
  await page.getByRole('button', { name: '로그인', exact: true }).click();
  await page.locator('input[type="text"]').click();
  await page.locator('input[type="text"]').fill('ll');
  await page.locator('input[type="text"]').press('Tab');
  await page.locator('input[type="password"]').fill('llll1');
  await page.locator('input[type="password"]').press('Enter');
  await page.locator('input[type="password"]').click();
  await page.locator('input[type="password"]').fill('llll1111');
  await page.locator('input[type="password"]').press('Enter');
  await page.getByRole('button', { name: '새 템플릿' }).click();
  await page.getByPlaceholder('제목을 입력해주세요').click();
  await page.getByRole('button', { name: '카테고리 정렬기준 펼침' }).click();
  await page.getByText('카테고리 4').click();
  await page.getByPlaceholder('제목을 입력해주세요').click();
  await page.getByPlaceholder('제목을 입력해주세요').fill('asdfasfd');
  await page.getByPlaceholder('이 템플릿을 언제 다시 쓸 것 같나요?').click();
  await page.getByPlaceholder('이 템플릿을 언제 다시 쓸 것 같나요?').fill('asdf');
  await page.getByPlaceholder('파일명.js').click();
  await page.getByPlaceholder('파일명.js').fill('asdf');
  await page
    .locator('div')
    .filter({ hasText: /^\/\/ 코드를 입력해주세요$/ })
    .nth(1)
    .click();
  await page.getByRole('button', { name: '저장' }).click();
});
