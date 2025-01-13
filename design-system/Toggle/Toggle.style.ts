import styled from '@emotion/styled';

import { theme } from '@design/style/theme';

export const ToggleContainer = styled.div`
  cursor: pointer;

  position: relative;

  overflow: hidden;
  display: flex;

  max-width: 12rem;
  height: 2.375rem; /* Button medium size와 동일 */

  background-color: ${theme.color.light.secondary_100};
  border-radius: 20px;
`;

export const ToggleOption = styled.div<{ selected: boolean }>`
  z-index: 1;

  display: flex;
  flex: 1;
  gap: 1px;
  align-items: center;
  justify-content: center;

  padding: 0 1rem;

  color: ${({ selected }) =>
    selected ? theme.color.light.white : theme.color.light.secondary_500};

  transition: color 0.3s ease;
`;

export const ToggleSlider = styled.div<{
  isRight: boolean;
  optionSliderColor: [string | undefined, string | undefined];
}>`
  position: absolute;
  top: 2px;
  left: 2px;
  transform: ${({ isRight }) =>
    isRight ? 'translateX(calc(100% - 4px))' : 'translateX(0)'};

  width: 50%;
  height: calc(100% - 4px);

  background-color: ${({
    isRight,
    optionSliderColor: [leftColor, rightColor],
  }) =>
    isRight
      ? (rightColor ?? theme.color.light.secondary_500)
      : (leftColor ?? theme.color.light.secondary_500)};
  border-radius: 18px;

  transition:
    transform 0.3s ease,
    background-color 0.3s ease;
`;
