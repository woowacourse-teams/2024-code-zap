import { Text } from '@/components';
import { theme } from '@/style/theme';
import * as S from './PagingButton.style';

interface PagingButtonProps {
  page: number;
  isActive: boolean;
  onClick: (page: number) => void;
}

const PagingButton = ({ page, isActive, onClick }: PagingButtonProps) => (
  <S.StyledPagingButton disabled={isActive} onClick={() => onClick(page)}>
    <Text.Medium color={theme.color.light.secondary_500}>{page}</Text.Medium>
  </S.StyledPagingButton>
);

export default PagingButton;
