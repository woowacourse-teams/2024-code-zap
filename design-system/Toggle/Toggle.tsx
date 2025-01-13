import { ReactNode } from 'react';

import * as S from './Toggle.style';

type ToggleOption<T extends string> = T;

export interface ToggleProps<T extends string> {
  showOptions?: boolean;
  options: [ToggleOption<T>, ToggleOption<T>];
  optionSliderColor?: [string | undefined, string | undefined];
  optionAdornments?: [ReactNode, ReactNode];
  selectedOption: ToggleOption<T>;
  switchOption: (option: ToggleOption<T>) => void;
}

const Toggle = <T extends string>({
  showOptions = true,
  options,
  optionAdornments = [undefined, undefined],
  optionSliderColor = [undefined, undefined],
  selectedOption,
  switchOption,
}: ToggleProps<T>) => {
  const [leftOption, rightOption] = options;
  const [leftOptionAdornment, rightOptionAdornment] = optionAdornments;

  const handleToggle = () => {
    const newOption = selectedOption === leftOption ? rightOption : leftOption;

    switchOption(newOption);
  };

  return (
    <S.ToggleContainer onClick={handleToggle}>
      <S.ToggleSlider isRight={selectedOption === rightOption} optionSliderColor={optionSliderColor} />
      <S.ToggleOption selected={selectedOption === leftOption}>
        {leftOptionAdornment ?? ''}
        {showOptions && leftOption}
      </S.ToggleOption>
      <S.ToggleOption selected={selectedOption === rightOption}>
        {showOptions && rightOption}
        {rightOptionAdornment ?? ''}
      </S.ToggleOption>
    </S.ToggleContainer>
  );
};

export default Toggle;
