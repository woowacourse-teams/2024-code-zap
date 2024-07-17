import { FontWeight } from './Text';

export const styleText = (
  size: string,
  weight: FontWeight = 'regular',
  color: string = '#ffffff'
) => {
  return {
    color: color,
    fontSize: `${size}rem`,
    fontWeight: weight === 'regular' ? 400 : 700,
  };
};
