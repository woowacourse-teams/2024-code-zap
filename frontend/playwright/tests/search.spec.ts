import { test, expect } from '@playwright/test';

import { searchTemplates } from './search.actions';
import { waitForSuccess } from './utils';

test('검색창에 `검색테스트`를 입력하면 `검색테스트`가 내용에 포함된 템플릿 목록을 확인할 수 있다.', async ({
  page,
}) => {
  await page.goto('/my-templates');

  const keyword = '검색테스트';

  await searchTemplates({ page, keyword });

  await waitForSuccess({ page, apiUrl: '/templates?keyword' });
  await expect(page.getByRole('link', { name: /검색테스트/ })).toBeVisible();
});

test('검색창에 `ㅁㅅㅌㅇ`를 입력할 경우 `검색 결과가 없습니다`가 나온다.', async ({ page }) => {
  await page.goto('/my-templates');

  const keyword = 'ㅁㅅㅌㅇ';

  await searchTemplates({ page, keyword });

  await waitForSuccess({ page, apiUrl: '/templates?keyword' });
  await expect(page.locator('div').filter({ hasText: /^검색 결과가 없습니다\.$/ })).toBeVisible();
});
