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

const colStyle = css({ display: 'flex', flexDirection: 'column', gap: '24px' });
const rowStyle = css({ display: 'flex', gap: '24px', alignItems: 'center' });

const buttonWrapper = css({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  width: '74px',
  height: '40px',
});

const ButtonGroup = ({ disabled }: { disabled: boolean }) => {
  return (
    <div css={colStyle}>
      <div css={rowStyle}>
        <div css={buttonWrapper}>
          <Button type='default' size='small' disabled={disabled}>
            버튼
          </Button>
        </div>
        <div css={buttonWrapper}>
          <Button type='default' size='medium' disabled={disabled}>
            버튼
          </Button>
        </div>
      </div>
      <div css={rowStyle}>
        <div css={buttonWrapper}>
          <Button type='outlined' size='small' disabled={disabled}>
            버튼
          </Button>
        </div>
        <div css={buttonWrapper}>
          <Button type='outlined' size='medium' disabled={disabled}>
            버튼
          </Button>
        </div>
      </div>
      <div css={rowStyle}>
        <div css={buttonWrapper}>
          <Button type='text' size='small' disabled={disabled}>
            버튼
          </Button>
        </div>
        <div css={buttonWrapper}>
          <Button type='text' size='medium' disabled={disabled}>
            버튼
          </Button>
        </div>
      </div>
    </div>
  );
};

export const Enabled: Story = {
  render: () => <ButtonGroup disabled={false} />,
};

export const Disabled: Story = {
  render: () => <ButtonGroup disabled={true} />,
};

export const CustomSized: Story = {
  render: () => {
    return (
      <div css={colStyle}>
        <Button type='default' width='100px'>
          100px
        </Button>
        <Button type='outlined' width='200px'>
          200px
        </Button>
        <Button type='text' width='300px'>
          300px
        </Button>
        <p style={{ color: '#888888', fontSize: '12px' }}>(text 타입 버튼의 사이즈는 텍스트 길이에 비례합니다.)</p>
      </div>
    );
  },
};
