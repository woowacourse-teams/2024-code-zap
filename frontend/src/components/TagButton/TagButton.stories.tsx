import type { Meta, StoryObj } from '@storybook/react';
import { useState } from 'react';

import TagButton from './TagButton';

const meta: Meta<typeof TagButton> = {
  title: 'TagButton',
  component: TagButton,
  args: {
    name: 'Tag',
    onClick: () => {},
  },
};

export default meta;

type Story = StoryObj<typeof TagButton>;

export const Enabled: Story = {
  decorators: [
    (Story, context) => {
      const [isFocused, setIsFocused] = useState(false);

      return (
        <div>
          <Story
            {...context}
            args={{
              ...context.args,
              isFocused,
              onClick: () => setIsFocused((prev) => !prev),
            }}
          />
        </div>
      );
    },
  ],
};

export const Disabled: Story = {
  args: {
    disabled: true,
  },
};

export const Focused: Story = {
  decorators: [
    (Story, context) => {
      const [isFocused, setIsFocused] = useState(true);

      return (
        <div>
          <Story
            {...context}
            args={{
              ...context.args,
              isFocused,
              onClick: () => setIsFocused((prev) => !prev),
            }}
          />
        </div>
      );
    },
  ],
};
