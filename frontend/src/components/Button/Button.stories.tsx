import { css } from '@emotion/react';
import Button from './Button';
import type { Meta, StoryObj } from '@storybook/react';

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
        <Button variant='default' size='small' disabled={disabled}>
          버튼
        </Button>
      </div>
      <div css={buttonWrapper}>
        <Button variant='default' size='medium' disabled={disabled}>
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

export const CustomSized: Story = {
  render: () => (
    <div css={colStyle}>
      <Button variant='default' width='10rem'>
        10rem
      </Button>
      <Button variant='outlined' width='20rem'>
        20rem
      </Button>
      <Button variant='text' width='30rem'>
        30rem
      </Button>
      <p style={{ color: '#888888', fontSize: '1.2rem' }}>(text 타입 버튼의 사이즈는 텍스트 길이에 비례합니다.)</p>
    </div>
  ),
};
