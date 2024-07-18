import { useState } from 'react';
import Input from './Input';
import type { Meta, StoryObj } from '@storybook/react';

const meta: Meta<typeof Input> = {
  title: 'Input',
  component: Input,
  args: {
    placeholder: 'Enter text',
    type: 'text',
    disabled: false,
  },
  argTypes: {
    type: {
      control: {
        type: 'select',
        options: ['text', 'email', 'password', 'search'],
      },
    },
  },
};

export default meta;

type Story = StoryObj<typeof Input>;

export const TextType: Story = {
  args: {
    type: 'text',
    placeholder: 'Enter text',
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return <Input {...args} value={value} onChange={(e) => setValue(e.target.value)} />;
  },
};

export const EmailType: Story = {
  args: {
    type: 'email',
    placeholder: 'Enter email',
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return <Input {...args} value={value} onChange={(e) => setValue(e.target.value)} />;
  },
};

export const PasswordType: Story = {
  args: {
    type: 'password',
    placeholder: 'Enter password',
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return <Input {...args} value={value} onChange={(e) => setValue(e.target.value)} />;
  },
};

export const SearchType: Story = {
  args: {
    type: 'search',
    placeholder: 'Search...',
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return <Input {...args} value={value} onChange={(e) => setValue(e.target.value)} />;
  },
};

export const Disabled: Story = {
  args: {
    disabled: true,
  },
};
