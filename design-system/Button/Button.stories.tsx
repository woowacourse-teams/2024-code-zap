import type { Meta, StoryObj } from '@storybook/react';
import { css } from '@emotion/react';

import Button, { ButtonVariant } from './Button';

const meta: Meta<typeof Button> = {
  title: 'Button',
  component: Button,
  args: {},
};

export default meta;

type Story = StoryObj<typeof Button>;

const colStyle = css({ display: 'flex', flexDirection: 'column', gap: '1rem' });
const rowStyle = css({ display: 'flex', gap: '2.4rem', alignItems: 'center' });

const buttonWrapper = css({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: '7.4rem',
});

const ButtonGroup = ({
  variant,
  disabled,
}: {
  variant: ButtonVariant;
  disabled: boolean;
}) => (
  <div css={colStyle}>
    <div css={rowStyle}>
      <div css={buttonWrapper}>
        <Button
          variant={variant}
          size='medium'
          weight='bold'
          disabled={disabled}
        >
          버튼
        </Button>
      </div>
      <div css={buttonWrapper}>
        <Button
          variant={variant}
          size='medium'
          weight='regular'
          disabled={disabled}
        >
          버튼
        </Button>
      </div>
    </div>
    <div css={rowStyle}>
      <div css={buttonWrapper}>
        <Button
          variant={variant}
          size='small'
          weight='bold'
          disabled={disabled}
        >
          버튼
        </Button>
      </div>
      <div css={buttonWrapper}>
        <Button
          variant={variant}
          size='small'
          weight='regular'
          disabled={disabled}
        >
          버튼
        </Button>
      </div>
    </div>
    <div css={rowStyle}>
      <div css={buttonWrapper}>
        <Button
          variant={variant}
          size='medium'
          weight='bold'
          fullWidth
          disabled={disabled}
        >
          버튼
        </Button>
      </div>
      <div css={buttonWrapper}>
        <Button
          variant={variant}
          size='medium'
          weight='regular'
          fullWidth
          disabled={disabled}
        >
          버튼
        </Button>
      </div>
    </div>
    <div css={rowStyle}>
      <div css={buttonWrapper}>
        <Button
          variant={variant}
          size='small'
          weight='bold'
          fullWidth
          disabled={disabled}
        >
          버튼
        </Button>
      </div>
      <div css={buttonWrapper}>
        <Button
          variant={variant}
          size='small'
          weight='regular'
          fullWidth
          disabled={disabled}
        >
          버튼
        </Button>
      </div>
    </div>
    <Button
      variant={variant}
      size='medium'
      weight='bold'
      fullWidth
      disabled={disabled}
    >
      버튼
    </Button>
    <Button
      variant={variant}
      size='small'
      weight='regular'
      fullWidth
      disabled={disabled}
    >
      버튼
    </Button>
  </div>
);

export const Contained: Story = {
  render: () => <ButtonGroup variant={'contained'} disabled={false} />,
};

export const Outlined: Story = {
  render: () => <ButtonGroup variant={'outlined'} disabled={false} />,
};

export const Text: Story = {
  render: () => <ButtonGroup variant={'text'} disabled={false} />,
};

export const Disabled: Story = {
  render: () => <ButtonGroup variant={'contained'} disabled={true} />,
};
