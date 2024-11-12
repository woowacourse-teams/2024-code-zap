import type { Meta, StoryObj } from '@storybook/react';

import PagingButtons from './PagingButtons';

const meta: Meta<typeof PagingButtons> = {
  title: 'PagingButtons',
  component: PagingButtons,
  args: {
    maxPages: 5,
    onPageChange: () => {},
  },
};

export default meta;

type Story = StoryObj<typeof PagingButtons>;

export const Page1of6: Story = {
  args: {
    currentPage: 1,
  },
};

export const Page2of6: Story = {
  args: {
    currentPage: 2,
  },
};

export const Page3of6: Story = {
  args: {
    currentPage: 3,
  },
};

export const Page4of6: Story = {
  args: {
    currentPage: 4,
  },
};

export const Page5of6: Story = {
  args: {
    currentPage: 5,
  },
};

export const Page6of6: Story = {
  args: {
    currentPage: 6,
  },
};
