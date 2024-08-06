import type { Meta, StoryObj } from '@storybook/react';

import { useDropdown } from '@/hooks/utils/useDropdown';
import Dropdown from './Dropdown';

const meta: Meta<typeof Dropdown> = {
  title: 'Dropdown',
  component: Dropdown,
};

export default meta;

type Story = StoryObj<typeof Dropdown>;

export const Default: Story = {
  args: {
    currentValue: '',
  },

  render: () => {
    const options = ['보기1', '보기2', '보기3'];
    const { isOpen, toggleDropdown, currentValue, handleCurrentValue, dropdownRef } =
      useDropdown<string>('선택해주세요');

    const props = {
      isOpen,
      dropdownRef,
      toggleDropdown,
      options,
      getOptionLabel: (string: string) => string,
      currentValue,
      handleCurrentValue,
    };

    return <Dropdown {...props} />;
  },
};
