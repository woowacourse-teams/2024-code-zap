import type { Meta, StoryObj } from '@storybook/react';
import { useState } from 'react';

import { SearchIcon } from '@/assets/images';
import Input from './Input';

const meta: Meta<typeof Input> = {
  title: 'Input',
  component: Input,
  argTypes: {
    variant: {
      control: {
        type: 'radio',
        options: ['filled', 'outlined', 'text'],
      },
    },
    size: {
      control: {
        type: 'radio',
        options: ['small', 'medium', 'large'],
      },
    },
    isValid: {
      control: {
        type: 'boolean',
      },
    },
  },
};

export default meta;

type Story = StoryObj<typeof Input>;

export const Filled: Story = {
  args: {
    variant: 'filled',
    size: 'medium',
    isValid: true,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Input size={args.size} variant={args.variant} isValid={args.isValid}>
          <Input.TextField placeholder='Enter Text' onChange={(e) => setValue(e.target.value)} value={value} />
        </Input>
      </div>
    );
  },
};

export const Outlined: Story = {
  args: {
    variant: 'outlined',
    size: 'medium',
    isValid: true,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Input size={args.size} variant={args.variant} isValid={args.isValid}>
          <Input.TextField placeholder='Enter Text' onChange={(e) => setValue(e.target.value)} value={value} />
        </Input>
      </div>
    );
  },
};

export const Text: Story = {
  args: {
    variant: 'text',
    size: 'medium',
    isValid: true,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Input size={args.size} variant={args.variant} isValid={args.isValid}>
          <Input.TextField placeholder='Enter Text' onChange={(e) => setValue(e.target.value)} value={value} />
        </Input>
      </div>
    );
  },
};

export const StartAdornment: Story = {
  args: {
    variant: 'outlined',
    size: 'medium',
    isValid: true,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Input size={args.size} variant={args.variant} isValid={args.isValid}>
          <Input.Adornment>
            <SearchIcon />
          </Input.Adornment>
          <Input.TextField placeholder='Enter Text' onChange={(e) => setValue(e.target.value)} value={value} />
        </Input>
      </div>
    );
  },
};

export const EndAdornment: Story = {
  args: {
    variant: 'outlined',
    size: 'medium',
    isValid: true,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Input size={args.size} variant={args.variant} isValid={args.isValid}>
          <Input.TextField placeholder='Enter Text' onChange={(e) => setValue(e.target.value)} value={value} />
          <Input.Adornment>
            <SearchIcon />
          </Input.Adornment>
        </Input>
      </div>
    );
  },
};

export const Invalid: Story = {
  args: {
    variant: 'outlined',
    size: 'medium',
    isValid: false,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Input size={args.size} variant={args.variant} isValid={args.isValid}>
          <Input.TextField placeholder='Enter Text' onChange={(e) => setValue(e.target.value)} value={value} />
        </Input>
      </div>
    );
  },
};

export const HelperText: Story = {
  args: {
    variant: 'outlined',
    size: 'medium',
    isValid: false,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Input size={args.size} variant={args.variant} isValid={args.isValid}>
          <Input.TextField placeholder='Enter Text' onChange={(e) => setValue(e.target.value)} value={value} />
          <Input.Adornment>
            <SearchIcon />
          </Input.Adornment>
          <Input.HelperText>설명입니다.</Input.HelperText>
        </Input>
      </div>
    );
  },
};
