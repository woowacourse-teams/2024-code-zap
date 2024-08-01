import type { Meta, StoryObj } from '@storybook/react';

import TagButton from './TagButton';

const meta: Meta<typeof TagButton> = {
  title: 'TagButton',
  component: TagButton,
  args: {
    id: 1,
    name: 'Tag',
    onClick: () => {},
  },
};

export default meta;

type Story = StoryObj<typeof TagButton>;

export const Enabled: Story = {};

export const Disabled: Story = {
  args: {
    disabled: true,
  },
};
