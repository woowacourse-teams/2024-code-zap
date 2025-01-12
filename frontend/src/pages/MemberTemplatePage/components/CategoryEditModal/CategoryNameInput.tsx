import { css } from '@emotion/react';

import { Input } from '@/components';
import { theme } from '@/style/theme';

interface CategoryNameInputProps {
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: () => void;
  onKeyDown: (e: React.KeyboardEvent) => void;
}

const CategoryNameInput = ({ value, onChange, onBlur, onKeyDown }: CategoryNameInputProps) => (
  <Input size='large' variant='text' style={{ width: '100%', height: '2.375rem' }}>
    <Input.TextField
      type='text'
      value={value}
      placeholder='카테고리 입력'
      onChange={onChange}
      onBlur={onBlur}
      onKeyDown={onKeyDown}
      autoFocus
      css={css`
        font-weight: bold;
        &::placeholder {
          font-weight: normal;
          color: ${theme.color.light.secondary_400};
        }
      `}
    />
  </Input>
);

export default CategoryNameInput;
