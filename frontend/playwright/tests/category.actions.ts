import { Page } from '@playwright/test';

interface Props {
  page: Page;
  categoryName: string;
}

export const getCategoryButton = ({ page, categoryName }: Props) => page.getByRole('button', { name: categoryName });

export const createCategory = async ({ page, categoryName }: Props) => {
  await page.getByRole('button', { name: '카테고리 편집' }).click();

  await page.getByRole('button', { name: '+ 카테고리 추가' }).click();
  await page.getByPlaceholder('카테고리 입력').fill(categoryName);
  await page.getByPlaceholder('카테고리 입력').press('Enter');

  await page.getByRole('button', { name: '저장' }).click();
};

export const deleteCategory = async ({ page, categoryName }: Props) => {
  await page.getByRole('button', { name: '카테고리 편집' }).click();

  const categoryInEditModal = page.getByText(categoryName).nth(1);

  await categoryInEditModal.hover();
  await page.getByRole('button', { name: '카테고리 삭제' }).click();

  await page.getByRole('button', { name: '저장' }).click();
};
