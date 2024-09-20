import { test, expect } from '@playwright/test';

import { createCategory, deleteCategory, getCategoryButton } from './category.actions';
import { waitForSuccess } from './utils';

test('카테고리 편집 모달에서 새 카테고리를 추가 및 삭제할 수 있다.', async ({ page, browserName }) => {
  await page.goto('/my-templates');

  const newCategoryName = `생성테스트-${browserName}`;

  await createCategory({ page, categoryName: newCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });

  const newCategoryButton = getCategoryButton({ page, categoryName: newCategoryName });

  await expect(newCategoryButton).toBeVisible();

  // 다음 테스트를 위해 테스트용 카테고리 삭제
  await deleteCategory({ page, categoryName: newCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });

  await expect(newCategoryButton).not.toBeVisible();
});

test('카테고리 편집 모달에서 카테고리명을 수정 및 삭제할 수 있다.', async ({ page, browserName }) => {
  await page.goto('/my-templates');

  const newCategoryName = `수정테스트-${browserName}`;
  const editedCategoryName = `수정완료-${browserName}`;

  // 수정할 카테고리 생성
  await createCategory({ page, categoryName: newCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });

  const newCategoryButton = getCategoryButton({ page, categoryName: newCategoryName });

  await expect(newCategoryButton).toBeVisible();

  // 카테고리 수정
  await page.getByRole('button', { name: '카테고리 편집' }).click();

  const newCategoryInEditModal = page.getByText(newCategoryName).nth(1);

  await newCategoryInEditModal.hover();
  await page.getByRole('button', { name: '카테고리 이름 변경' }).click();
  await page.getByPlaceholder('카테고리 입력').click();
  await page.getByPlaceholder('카테고리 입력').fill(editedCategoryName);
  await page.getByRole('button', { name: '저장' }).click();

  const editedCategoryButton = getCategoryButton({ page, categoryName: editedCategoryName });

  await expect(editedCategoryButton).toBeVisible();

  // 다음 테스트를 위해 테스트용 카테고리 삭제
  await deleteCategory({ page, categoryName: editedCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });
  await expect(editedCategoryButton).not.toBeVisible();
});

test('카테고리는 최대 15글자까지만 입력할 수 있다.', async ({ page, browserName }) => {
  await page.goto('/my-templates');

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

  const newCategoryButton = getCategoryButton({ page, categoryName: expectedCategoryName });

  await expect(newCategoryButton).toBeVisible();

  // 다음 테스트를 위해 테스트용 카테고리 삭제
  await deleteCategory({ page, categoryName: expectedCategoryName });

  await waitForSuccess({ page, apiUrl: '/categories' });
  await expect(newCategoryButton).not.toBeVisible();
});
