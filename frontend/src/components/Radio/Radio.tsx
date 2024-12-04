import { Text } from '@/components';
import { theme } from '@/style/theme';

import * as S from './Radio.style';

interface Props<T> {
  options: T[];
  currentValue: T;
  getOptionLabel: (option: T) => string;
  handleCurrentValue: (value: T) => void;
}

const Radio = <T,>({ options, currentValue, getOptionLabel, handleCurrentValue }: Props<T>) => (
  <S.RadioContainer>
    {options.map((option) => (
      <S.RadioOption key={getOptionLabel(option)} onClick={() => handleCurrentValue(option)}>
        <S.RadioCircle isSelected={currentValue === option} />
        <Text.Medium color={theme.color.light.secondary_800}>{getOptionLabel(option)}</Text.Medium>
      </S.RadioOption>
    ))}
  </S.RadioContainer>
);

export default Radio;
