import type { Meta, StoryObj } from '@storybook/react';

import { tags } from '@/mocks/tagList.json';

import TagFilterMenu from './TagFilterMenu';

const meta: Meta<typeof TagFilterMenu> = {
  title: 'TagFilterMenu',
  component: TagFilterMenu,
  args: { tagList: tags },
};

export default meta;

type Story = StoryObj<typeof TagFilterMenu>;

export const Default: Story = {
  args: {
    selectedTagIds: [],
  },
};

export const Selected: Story = {
  args: {
    selectedTagIds: [1, 5, 6, 9],
  },
};
