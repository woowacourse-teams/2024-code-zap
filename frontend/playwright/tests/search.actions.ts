import { Page } from '@playwright/test';

interface Props {
  page: Page;
  keyword: string;
}

export const searchTemplates = async ({ page, keyword }: Props) => {
  const searchInput = page.getByPlaceholder('검색');

  await searchInput.waitFor({ state: 'visible' });
  await searchInput.click();
  await searchInput.fill(keyword);
  await searchInput.press('Enter');
};
