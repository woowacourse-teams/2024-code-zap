import type { Meta, StoryObj } from '@storybook/react';

import { templates } from '@/mocks/templateList.json';
import TemplateCard from './TemplateCard';

const meta: Meta<typeof TemplateCard> = {
  title: 'TemplateCard',
  component: TemplateCard,
  args: {
    template: templates[0],
  },
};

export default meta;

type Story = StoryObj<typeof TemplateCard>;

export const Default: Story = {};
