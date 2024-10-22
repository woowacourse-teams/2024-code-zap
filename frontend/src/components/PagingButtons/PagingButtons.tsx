import { Text } from '@/components';
import { trackMyTemplatePaging } from '@/service/amplitude/track';
import { theme } from '@/style/theme';

import * as S from './PagingButtons.style';

interface Props {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
}

const PagingButtons = ({ currentPage, totalPages, onPageChange }: Props) => {
  const getPageNumbers = () => {
    const startPage = Math.max(1, Math.min(currentPage - 2, totalPages - 4));
    const endPage = Math.min(totalPages, startPage + 4);

    return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
  };

  const handlePagingClick = (page: number, label: string) => {
    trackMyTemplatePaging({ page, totalPages, label });
    onPageChange(page);
  };

  return (
    <S.PagingContainer>
      <PagingButton page={1} disabled={currentPage === 1} onClick={handlePagingClick} label='<<' />
      <PagingButton page={currentPage - 1} disabled={currentPage === 1} onClick={handlePagingClick} label='<' />

      {getPageNumbers().map((page) => (
        <PagingButton
          key={page}
          page={page}
          isActive={page === currentPage}
          onClick={handlePagingClick}
          label={String(page)}
        />
      ))}

      <PagingButton
        page={currentPage + 1}
        disabled={currentPage === totalPages}
        onClick={handlePagingClick}
        label='>'
      />
      <PagingButton page={totalPages} disabled={currentPage === totalPages} onClick={handlePagingClick} label='>>' />
    </S.PagingContainer>
  );
};

interface PagingButtonProps {
  page?: number;
  isActive?: boolean;
  disabled?: boolean;
  onClick: (page: number, label: string) => void;
  label: string;
}

const PagingButton = ({ page, isActive, disabled, onClick, label }: PagingButtonProps) => (
  <S.PagingButton isActive={isActive} disabled={disabled || isActive} onClick={() => onClick(page ?? 1, label)}>
    <Text.Small color={isActive ? theme.color.light.white : theme.color.light.secondary_500}>{label}</Text.Small>
  </S.PagingButton>
);

export default PagingButtons;
