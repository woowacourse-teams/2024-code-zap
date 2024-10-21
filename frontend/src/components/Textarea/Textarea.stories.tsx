import type { Meta, StoryObj } from '@storybook/react';
import { useState, ReactNode } from 'react';

import Textarea, { BaseProps, TextFieldProps } from './Textarea';

type TextareaStoryProps = BaseProps & {
  children?: ReactNode;
  minRows?: TextFieldProps['minRows'];
  maxRows?: TextFieldProps['maxRows'];
};

const meta: Meta<TextareaStoryProps> = {
  title: 'Textarea',
  component: Textarea,
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
        options: ['small', 'medium', 'large', 'xlarge'],
      },
    },
    isValid: {
      control: {
        type: 'boolean',
      },
    },
    minRows: {
      control: {
        type: 'number',
        min: 1,
      },
    },
    maxRows: {
      control: {
        type: 'number',
        min: 1,
      },
    },
  },
};

export default meta;

type Story = StoryObj<TextareaStoryProps>;

export const Filled: Story = {
  args: {
    minRows: 3,
    maxRows: 8,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Textarea variant={args.variant} size={args.size} isValid={args.isValid}>
          <Textarea.TextField
            placeholder='Enter Text'
            onChange={(e) => setValue(e.target.value)}
            value={value}
            minRows={args.minRows}
            maxRows={args.maxRows}
          />
        </Textarea>
      </div>
    );
  },
};

export const Outlined: Story = {
  args: {
    variant: 'outlined',
    minRows: 3,
    maxRows: 8,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Textarea variant={args.variant} size={args.size} isValid={args.isValid}>
          <Textarea.TextField
            placeholder='Enter Text'
            onChange={(e) => setValue(e.target.value)}
            value={value}
            minRows={args.minRows}
            maxRows={args.maxRows}
          />
        </Textarea>
      </div>
    );
  },
};

export const Text: Story = {
  args: {
    variant: 'text',
    minRows: 3,
    maxRows: 8,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Textarea variant={args.variant} size={args.size} isValid={args.isValid}>
          <Textarea.TextField
            placeholder='Enter Text'
            onChange={(e) => setValue(e.target.value)}
            value={value}
            minRows={args.minRows}
            maxRows={args.maxRows}
          />
        </Textarea>
      </div>
    );
  },
};

export const WithLabel: Story = {
  args: {
    variant: 'outlined',
    minRows: 3,
    maxRows: 8,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Textarea id='helper' variant={args.variant} size={args.size} isValid={args.isValid}>
          <Textarea.Label htmlFor='helper'>라벨</Textarea.Label>
          <Textarea.TextField
            placeholder='Enter Text'
            onChange={(e) => setValue(e.target.value)}
            value={value}
            minRows={args.minRows}
            maxRows={args.maxRows}
          />
        </Textarea>
      </div>
    );
  },
};

export const WithHelperText: Story = {
  args: {
    variant: 'outlined',
    size: 'medium',
    isValid: false,
    minRows: 3,
    maxRows: 8,
  },
  render: (args) => {
    const [value, setValue] = useState('');

    return (
      <div style={{ width: '500px' }}>
        <Textarea id='helper' variant={args.variant} size={args.size} isValid={args.isValid}>
          <Textarea.Label htmlFor='helper'>라벨</Textarea.Label>
          <Textarea.TextField
            placeholder='Enter Text'
            onChange={(e) => setValue(e.target.value)}
            value={value}
            minRows={args.minRows}
            maxRows={args.maxRows}
          />
          <Textarea.HelperText>설명입니다</Textarea.HelperText>
        </Textarea>
      </div>
    );
  },
};
