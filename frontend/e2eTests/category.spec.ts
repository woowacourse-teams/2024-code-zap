import { test, expect, Page } from '@playwright/test';

import { loginToCodezap, waitForSuccess } from './testUtils';

test.beforeEach(async ({ page }) => {
  await loginToCodezap({ page, username: 'll', password: 'llll1111' });
});

test('카테고리 편집 모달에서 새 카테고리를 추가 및 삭제할 수 있다.', async ({ page, browserName }) => {
  const newCategoryName = `생성테스트-${browserName}`;

  await createCategory({ page, newCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });
  await expect(page.getByRole('button', { name: newCategoryName })).toBeVisible();

  await deleteCategory({ page, newCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });
  await expect(page.getByRole('button', { name: newCategoryName })).not.toBeVisible();
});

test('카테고리 편집 모달에서 카테고리명을 수정 및 삭제할 수 있다.', async ({ page, browserName }) => {
  const newCategoryName = `수정테스트-${browserName}`;
  const editedCategoryName = `수정완료-${browserName}`;

  await createCategory({ page, newCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });
  await expect(page.getByRole('button', { name: newCategoryName })).toBeVisible();

  await page.getByRole('button', { name: '카테고리 편집' }).click();

  await page.getByText(newCategoryName).nth(1).hover();
  await page.getByRole('button', { name: '카테고리 이름 변경' }).click();
  await page.getByPlaceholder('카테고리 입력').click();
  await page.getByPlaceholder('카테고리 입력').fill(editedCategoryName);
  await page.getByRole('button', { name: '저장' }).click();

  await expect(page.getByRole('button', { name: editedCategoryName })).toBeVisible();

  await deleteCategory({ page, newCategoryName: editedCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });
  await expect(page.getByRole('button', { name: editedCategoryName })).not.toBeVisible();
});

test('카테고리는 최대 15글자까지만 입력할 수 있다.', async ({ page, browserName }) => {
  const rawCategoryName = `최대글자수테스트-${browserName}`;
  const expectedCategoryName = rawCategoryName.slice(0, 15);

  await page.getByRole('button', { name: '카테고리 편집' }).click();

  await page.getByRole('button', { name: '+ 카테고리 추가' }).click();
  const categoryInput = page.getByPlaceholder('카테고리 입력');

  await categoryInput.click();

  for (const char of rawCategoryName) {
    await page.keyboard.type(char);
  }

  await page.getByRole('button', { name: '저장' }).click();

  await waitForSuccess({ page, apiUrl: '/categories' });
  await expect(page.getByRole('button', { name: expectedCategoryName })).toBeVisible();

  await deleteCategory({ page, newCategoryName: expectedCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });
  await expect(page.getByRole('button', { name: expectedCategoryName })).not.toBeVisible();
});

interface Props {
  page: Page;
  newCategoryName: string;
}

const createCategory = async ({ page, newCategoryName }: Props) => {
  await page.getByRole('button', { name: '카테고리 편집' }).click();

  await page.getByRole('button', { name: '+ 카테고리 추가' }).click();
  await page.getByPlaceholder('카테고리 입력').fill(newCategoryName);
  await page.getByPlaceholder('카테고리 입력').press('Enter');

  await page.getByRole('button', { name: '저장' }).click();
};

const deleteCategory = async ({ page, newCategoryName }: Props) => {
  await page.getByRole('button', { name: '카테고리 편집' }).click();

  await page.getByText(newCategoryName).nth(1).hover();
  await page.getByRole('button', { name: '카테고리 삭제' }).click();

  await page.getByRole('button', { name: '저장' }).click();
};
