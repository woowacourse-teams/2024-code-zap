import type { Meta, StoryObj } from '@storybook/react';

import { templates } from '@/mocks/templateList.json';
import TemplateGrid from './TemplateGrid';

const meta: Meta<typeof TemplateGrid> = {
  title: 'TemplateGrid',
  component: TemplateGrid,
  args: {
    templates,
  },
};

export default meta;

type Story = StoryObj<typeof TemplateGrid>;

export const Column1: Story = {
  args: { cols: 1 },
};

export const Column2: Story = {
  args: { cols: 2 },
};

export const Column3: Story = {
  args: { cols: 3 },
};
