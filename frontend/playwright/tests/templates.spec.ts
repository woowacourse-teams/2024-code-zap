import { expect, test } from '@playwright/test';

import { deleteTemplate, uploadTemplateToCodezap } from './templates.actions';
import { waitForSuccess } from './utils';

test('템플릿 업로드 시, 파일명을 입력하지 않으면 `파일명을 입력해주세요`라는 토스트 메시지가 나온다.', async ({
  page,
}) => {
  await page.goto('/');
  await page.getByRole('button', { name: '새 템플릿' }).click();
  await page.getByPlaceholder('제목을 입력해주세요').fill('템플릿생성테스트');
  await page.getByPlaceholder('이 템플릿을 언제 다시 쓸 것 같나요?').fill('템플릿생성테스트');
  await page.getByRole('button', { name: '저장' }).click();

  const toastMessage = page.locator('text=파일명을 입력해주세요');

  await expect(toastMessage).toBeVisible();
});

test('템플릿 제목, 설명, 파일명, 소스코드, 태그를 입력하고 저장버튼을 눌러 템플릿을 생성한다. 목록 페이지에서 새로 생성된 제목의 템플릿 카드를 확인할 수 있다.', async ({
  page,
  browserName,
}) => {
  await page.goto('/my-templates');

  const testTitle = `템플릿생성테스트_${browserName}`;

  try {
    await uploadTemplateToCodezap({
      page,
      title: testTitle,
      fileName: testTitle,
      code: testTitle,
      description: testTitle,
      tag: testTitle,
    });

    const templateCard = page.getByRole('link', { name: testTitle }).first();

    await expect(templateCard).toBeVisible();
  } catch (error) {
    throw Error(error);
  } finally {
    await deleteTemplate({ page, templateName: testTitle });
  }
});

test('템플릿 카드를 누르면 템플릿 제목, 설명, 작성자, 생성날짜, 변경날짜, 카테고리, 코드 스니펫 목록을 확인할 수 있다.', async ({
  page,
}) => {
  await page.goto('/my-templates');
  // 템플릿 목록
  await waitForSuccess({ page, apiUrl: '/templates?keyword' });

  const templateCard = page.getByRole('link', { name: '상세조회테스트' });

  await expect(templateCard).toBeVisible();
  await templateCard.click();

  const title = page.getByText('상세조회테스트').first();
  const name = page.getByText('ll', { exact: true });
  const editedDate = page.getByText('2024년 9월 20일');
  const createdDate = page.getByText('(2024년 8월 21일)');
  const tag = page.getByRole('button', { name: '테스트' });
  const filename = page
    .locator('div')
    .filter({ hasText: /^test.ts$/ })
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
  browserName,
}) => {
  await page.goto('/my-templates');

  const beforeTemplateTitle = `템플릿편집테스트-${browserName}`;
  const afterTemplateTitle = `편집된템플릿-${browserName}`;
  const addedTagName = `추가된태그-${browserName}`;

  try {
    await uploadTemplateToCodezap({
      page,
      title: beforeTemplateTitle,
      fileName: beforeTemplateTitle,
      code: beforeTemplateTitle,
      description: beforeTemplateTitle,
      tag: beforeTemplateTitle,
    });

    await page.getByRole('link', { name: beforeTemplateTitle }).first().click();

    await page.getByRole('button', { name: '템플릿 편집' }).click();
    await page.getByPlaceholder('제목을 입력해주세요').fill(afterTemplateTitle);
    await page.getByPlaceholder('enter 또는 space bar로 태그를 등록해보세요').fill(addedTagName);
    await page.getByPlaceholder('enter 또는 space bar로 태그를 등록해보세요').press('Enter');
    await page.getByRole('button', { name: '저장' }).click();

    await page.goto('/my-templates');

    await expect(page.getByText(afterTemplateTitle).first()).toBeVisible();
    await expect(page.getByRole('button', { name: addedTagName }).first()).toBeVisible();
  } catch (error) {
    throw Error(error);
  } finally {
    await deleteTemplate({ page, templateName: afterTemplateTitle });
  }
});

test('템플릿 삭제 버튼을 누르면 삭제 확인 모달이 뜨고, 삭제 확인 모달에서 삭제 버튼을 누르면, 템플릿이 삭제되고 내탬플릿 화면으로 이동한다.', async ({
  page,
  browserName,
}) => {
  await page.goto('/my-templates');

  const testTitle = `템플릿삭제테스트-${browserName}`;

  await uploadTemplateToCodezap({
    page,
    title: testTitle,
    fileName: testTitle,
    code: testTitle,
    description: testTitle,
    tag: testTitle,
  });

  await page.getByRole('link', { name: testTitle }).first().click();
  await page.getByRole('button', { name: '템플릿 삭제' }).click();

  await expect(page.getByText('정말 삭제하시겠습니까?')).toBeVisible();

  await page.getByRole('button', { name: '삭제', exact: true }).click();

  await expect(page.getByRole('link', { name: testTitle })).not.toBeVisible();
});
