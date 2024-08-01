import type { Meta, StoryObj } from '@storybook/react';

import { categories } from '@/mocks/categoryList.json';
import CategoryButton from './CategoryButton';

const meta: Meta<typeof CategoryButton> = {
  title: 'CategoryButton',
  component: CategoryButton,
  args: { category: categories[0] },
};

export default meta;

type Story = StoryObj<typeof CategoryButton>;

export const Default: Story = {};
