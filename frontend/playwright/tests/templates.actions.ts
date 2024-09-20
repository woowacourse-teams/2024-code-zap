import { expect, Page } from '@playwright/test';

import { waitForSuccess } from './utils';

/**
 * 
 description과 tag는 필수 입력이 아닙니다.
 */
export const uploadTemplateToCodezap = async ({
  page,
  title,
  fileName,
  code,
  description,
  tag,
}: {
  page: Page;
  title: string;
  fileName: string;
  code: string;
  description?: string;
  tag?: string;
}) => {
  // 유저의 카테고리 리스트
  await page.waitForLoadState('networkidle');

  await page.getByRole('button', { name: '새 템플릿' }).click();

  // 제목 입력
  await page.getByPlaceholder('제목을 입력해주세요').fill(title);

  // 설명 입력
  if (description) {
    await page.getByPlaceholder('이 템플릿을 언제 다시 쓸 것 같나요?').fill(description);
  }

  // 파일명 입력
  await page.getByPlaceholder('파일명.js').fill(fileName);

  // 코드 입력
  await page
    .locator('div')
    .filter({ hasText: /^\/\/ 코드를 입력해주세요$/ })
    .nth(1)
    .fill(code);

  // 태그 입력
  if (tag) {
    await page.getByPlaceholder('enter 또는 space bar로 태그를 등록해보세요').fill(tag);
    await page.getByPlaceholder('enter 또는 space bar로 태그를 등록해보세요').press('Enter');
  }

  // 저장 버튼 클릭
  await page.getByRole('button', { name: '저장' }).click();

  await waitForSuccess({ page, apiUrl: '/templates' });

  await expect(page).toHaveURL('/my-templates');
};
