import { expect, test } from '@playwright/test';

import { uploadTemplateToCodezap } from './templates.actions';
import { loginToCodezap, waitForSuccess } from './utils';

// 로그인 동작을 모든 테스트 전에 실행
test.beforeEach(async ({ page }) => {
  await loginToCodezap({ page, username: 'll', password: 'llll1111' });
});

test('템플릿 업로드 시, 파일명을 입력하지 않으면 `파일명을 입력해주세요`라는 토스트 메시지가 나온다.', async ({
  page,
}) => {
  await page.getByRole('button', { name: '새 템플릿' }).click();
  await page.getByPlaceholder('제목을 입력해주세요').fill('템플릿생성테스트');
  await page.getByPlaceholder('이 템플릿을 언제 다시 쓸 것 같나요?').fill('템플릿생성테스트');
  await page.getByRole('button', { name: '저장' }).click();

  const toastMessage = page.locator('text=파일명을 입력해주세요');

  await expect(toastMessage).toBeVisible();
});

test('템플릿 제목, 설명, 파일명, 소스코드, 태그를 입력하고 저장버튼을 눌러 템플릿을 생성한다. 목록 페이지에서 새로 생성된 제목의 템플릿 카드를 확인할 수 있다.', async ({
  page,
}) => {
  // 유저의 카테고리 리스트
  await waitForSuccess({ page, apiUrl: '/categories' });

  await uploadTemplateToCodezap({
    page,
    title: '템플릿생성테스트',
    fileName: '템플릿생성테스트',
    code: '템플릿생성테스트',
    description: '템플릿생성테스트',
    tag: '템플릿생성테스트',
  });

  const templateCard = page
    .getByRole('link', { name: 'll 방금 전 템플릿생성테스트 템플릿생성테스트 템플릿생성테스트 템플릿생성테스트' })
    .first();

  await expect(templateCard).toBeVisible();
});

test('템플릿 카드를 누르면 템플릿 제목, 설명, 작성자, 생성날짜, 변경날짜, 카테고리, 코드 스니펫 목록을 확인할 수 있다.', async ({
  page,
}) => {
  // 템플릿 목록
  await waitForSuccess({ page, apiUrl: '/templates' });

  const templateCard = page.getByRole('link', { name: 'll 2024년 9월 12일 테스트2' });

  await expect(templateCard).toBeVisible();
  await templateCard.click();

  const title = page.getByText('테스트2').first();
  const name = page.getByText('ll', { exact: true });
  const editedDate = page.getByText('2024년 9월 12일');
  const createdDate = page.getByText('(2024년 9월 11일)');
  const tag = page.getByRole('button', { name: 'test' });
  const filename = page
    .locator('div')
    .filter({ hasText: /^test2.ts$/ })
    .nth(1);
  const sourceCodes = page.getByRole('textbox').getByText('// 함수');

  await expect(title).toBeVisible();
  await expect(name).toBeVisible();
  await expect(editedDate).toBeVisible();
  await expect(createdDate).toBeVisible();
  await expect(tag).toBeVisible();
  await expect(filename).toBeVisible();
  await expect(sourceCodes).toBeVisible();
});

test('`템플릿편집테스트` 템플릿의 제목을 `편집된템플릿`로 변경하고, `편집된템플릿`태그를 추가로 등록한다.', async ({
  page,
}) => {
  await uploadTemplateToCodezap({
    page,
    title: '템플릿편집테스트',
    fileName: '템플릿편집테스트',
    code: '템플릿편집테스트',
    description: '템플릿편집테스트',
    tag: '템플릿편집테스트',
  });

  await page
    .getByRole('link', { name: 'll 방금 전 템플릿편집테스트 템플릿편집테스트 템플릿편집테스트' })
    .first()
    .click();

  await page.getByRole('button', { name: '템플릿 편집' }).click();
  await page.getByPlaceholder('제목을 입력해주세요').fill('편집된템플릿');
  await page.getByPlaceholder('enter 또는 space bar로 태그를 등록해보세요').fill('편집된템플릿');
  await page.getByPlaceholder('enter 또는 space bar로 태그를 등록해보세요').press('Enter');
  await page.getByRole('button', { name: '저장' }).click();

  await expect(page.getByText('편집된템플릿').first()).toBeVisible();
  await expect(page.getByRole('button', { name: '편집된템플릿' })).toBeVisible();
});

test('템플릿 삭제 버튼을 누르면 삭제 확인 모달이 뜨고, 삭제 확인 모달에서 삭제 버튼을 누르면, 템플릿이 삭제되고 내탬플릿 화면으로 이동한다.', async ({
  page,
}) => {
  await uploadTemplateToCodezap({
    page,
    title: '템플릿삭제테스트',
    fileName: '템플릿삭제테스트',
    code: '템플릿삭제테스트',
    description: '템플릿삭제테스트',
    tag: '템플릿삭제테스트',
  });

  await page
    .getByRole('link', { name: 'll 방금 전 템플릿삭제테스트 템플릿삭제테스트 템플릿삭제테스트 템플릿삭제테스트' })
    .first()
    .click();

  await page.getByRole('button', { name: '템플릿 삭제' }).click();
  await expect(page.getByText('정말 삭제하시겠습니까?')).toBeVisible();

  await page.getByRole('button', { name: '삭제', exact: true }).click();
  await expect(
    page.getByRole('link', { name: 'll 방금 전 템플릿삭제테스트 템플릿삭제테스트 템플릿삭제테스트 모든 태그' }),
  ).not.toBeVisible();
});
