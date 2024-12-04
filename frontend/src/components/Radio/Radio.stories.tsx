import type { Meta, StoryObj } from '@storybook/react';
import { useState } from 'react';

import Radio from './Radio';

const meta: Meta<typeof Radio> = {
  title: 'Radio',
  component: Radio,
};

export default meta;

type Story = StoryObj<typeof Radio>;

export const Default: Story = {
  render: () => {
    const options = ['코', '드', '잽'];
    const [currentValue, setCurrentValue] = useState(options[0]);

    return (
      <Radio
        options={options}
        currentValue={currentValue}
        getOptionLabel={(option: string) => option}
        handleCurrentValue={setCurrentValue}
      />
    );
  },
};
