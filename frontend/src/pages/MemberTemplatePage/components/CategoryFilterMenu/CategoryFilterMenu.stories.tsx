import type { Meta, StoryObj } from '@storybook/react';

import { categories } from '@/mocks/fixtures/categoryList.json';

import CategoryFilterMenu from './CategoryFilterMenu';

const meta: Meta<typeof CategoryFilterMenu> = {
  title: 'CategoryFilterMenu',
  component: CategoryFilterMenu,
  args: { categoryList: categories },
};

export default meta;

type Story = StoryObj<typeof CategoryFilterMenu>;

export const Default: Story = {};
