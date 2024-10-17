import type { Meta, StoryObj } from '@storybook/react';
import { useState } from 'react';

import Toggle from './Toggle';

const meta: Meta<typeof Toggle> = {
  title: 'Toggle',
  component: Toggle,
};

export default meta;

type Story = StoryObj<typeof Toggle>;

export const Default: Story = {
  args: {
    options: ['private', 'public'],
    selectedOption: 'private',
  },

  render: (args) => {
    const [selectedOption, setSelectedOption] = useState(args.selectedOption);

    const handleToggle = (option: string) => {
      setSelectedOption(option);
    };

    return <Toggle options={args.options} selectedOption={selectedOption} switchOption={handleToggle} />;
  },
};
