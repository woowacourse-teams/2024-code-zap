import type { Meta, StoryObj } from '@storybook/react';

import { categories } from '@/mocks/categoryList.json';
import CategoryMenu from './CategoryMenu';

const meta: Meta<typeof CategoryMenu> = {
  title: 'CategoryMenu',
  component: CategoryMenu,
  args: { categories },
};

export default meta;

type Story = StoryObj<typeof CategoryMenu>;

export const Default: Story = {};
