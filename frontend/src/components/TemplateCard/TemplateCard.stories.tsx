import type { Meta, StoryObj } from '@storybook/react';

import { templates } from '@/mocks/fixtures/templateList.json';
import { TemplateListItem } from '@/types';

import TemplateCard from './TemplateCard';

const meta: Meta<typeof TemplateCard> = {
  title: 'TemplateCard',
  component: TemplateCard,
  args: {
    template: templates[0] as TemplateListItem,
  },
};

export default meta;

type Story = StoryObj<typeof TemplateCard>;

export const Default: Story = {};
