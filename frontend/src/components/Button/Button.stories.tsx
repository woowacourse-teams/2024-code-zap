import type { Meta, StoryObj } from '@storybook/react';
import { css } from '@emotion/react';

import Button from './Button';

const meta: Meta<typeof Button> = {
  title: 'Button',
  component: Button,
  args: {},
};

export default meta;

type Story = StoryObj<typeof Button>;

const colStyle = css({ display: 'flex', flexDirection: 'column', gap: '2.4rem' });
const rowStyle = css({ display: 'flex', gap: '2.4rem', alignItems: 'center' });

const buttonWrapper = css({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: '7.4rem',
  height: '4rem',
});

const ButtonGroup = ({ disabled }: { disabled: boolean }) => (
  <div css={colStyle}>
    <div css={rowStyle}>
      <div css={buttonWrapper}>
        <Button variant='contained' size='small' disabled={disabled}>
          버튼
        </Button>
      </div>
      <div css={buttonWrapper}>
        <Button variant='contained' size='medium' disabled={disabled}>
          버튼
        </Button>
      </div>
    </div>
    <div css={rowStyle}>
      <div css={buttonWrapper}>
        <Button variant='outlined' size='small' disabled={disabled}>
          버튼
        </Button>
      </div>
      <div css={buttonWrapper}>
        <Button variant='outlined' size='medium' disabled={disabled}>
          버튼
        </Button>
      </div>
    </div>
    <div css={rowStyle}>
      <div css={buttonWrapper}>
        <Button variant='text' size='small' disabled={disabled}>
          버튼
        </Button>
      </div>
      <div css={buttonWrapper}>
        <Button variant='text' size='medium' disabled={disabled}>
          버튼
        </Button>
      </div>
    </div>
  </div>
);

export const Enabled: Story = {
  render: () => <ButtonGroup disabled={false} />,
};

export const Disabled: Story = {
  render: () => <ButtonGroup disabled={true} />,
};
