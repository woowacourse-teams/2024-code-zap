import { Text } from '@/components';
import { theme } from '@design/style/theme';

import * as S from './Radio.style';

interface Props<T extends string> {
  options: Record<T, T | string | number>;
  currentValue: T;
  handleCurrentValue: (value: T) => void;
}

const Radio = <T extends string>({
  options,
  currentValue,
  handleCurrentValue,
}: Props<T>) => (
  <S.RadioContainer>
    {Object.keys(options).map((optionKey) => (
      <S.RadioOption
        key={optionKey}
        onClick={() => handleCurrentValue(optionKey as T)}
      >
        <S.RadioCircle isSelected={currentValue === optionKey} />
        <Text.Medium color={theme.color.light.secondary_800}>
          {options[optionKey as T]}
        </Text.Medium>
      </S.RadioOption>
    ))}
  </S.RadioContainer>
);

export default Radio;
