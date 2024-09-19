import { test, expect, Page } from '@playwright/test';

import { loginToCodezap, waitForSuccess } from './testUtils';

test.beforeEach(async ({ page }) => {
  await loginToCodezap({ page, username: 'll', password: 'llll1111' });
});

test('검색창에 `테스트`를 입력하면 `테스트`가 내용에 포함된 템플릿 목록을 확인할 수 있다.', async ({ page }) => {
  const keyword = '테스트';

  await searchTemplates({ page, keyword });

  await waitForSuccess({ page, apiUrl: '/templates?keyword' });
  await expect(page.getByRole('link', { name: /테스트2/ })).toBeVisible();
});

test('검색창에 `ㅁㅅㅌㅇ`를 입력할 경우 `검색 결과가 없습니다`가 나온다.', async ({ page }) => {
  const keyword = 'ㅁㅅㅌㅇ';

  await searchTemplates({ page, keyword });

  await waitForSuccess({ page, apiUrl: '/templates?keyword' });
  await expect(page.locator('div').filter({ hasText: /^검색 결과가 없습니다\.$/ })).toBeVisible();
});

interface Props {
  page: Page;
  keyword: string;
}

const searchTemplates = async ({ page, keyword }: Props) => {
  const searchInput = page.getByPlaceholder('검색');

  await searchInput.waitFor({ state: 'visible' });
  await searchInput.click();
  await searchInput.fill(keyword);
  await searchInput.press('Enter');
};
