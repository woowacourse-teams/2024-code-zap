import type { Meta, StoryObj } from '@storybook/react';

import PagingButton from './PagingButton';

const meta: Meta<typeof PagingButton> = {
  title: 'PagingButton',
  component: PagingButton,
};

export default meta;

type Story = StoryObj<typeof PagingButton>;

export const Default: Story = {
  render: () => (
    <div>
      <PagingButton page={1} isActive={false} onClick={() => {}} />,
      <PagingButton page={2} isActive={false} onClick={() => {}} />,
      <PagingButton page={3} isActive={false} onClick={() => {}} />,
    </div>
  ),
};

export const Active: Story = {
  render: () => (
    <div>
      <PagingButton page={1} isActive={true} onClick={() => {}} />,
      <PagingButton page={2} isActive={true} onClick={() => {}} />,
      <PagingButton page={3} isActive={true} onClick={() => {}} />,
    </div>
  ),
};
