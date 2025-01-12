import { PencilIcon, SpinArrowIcon, TrashcanIcon } from '@/assets/images';
import { ICON_SIZE } from '@/style/styleConstants';

import * as S from './CategoryEditModal.style';

interface IconButtonsProps {
  onRestoreClick?: () => void;
  onEditClick?: () => void;
  onDeleteClick?: () => void;
  restore?: boolean;
  edit?: boolean;
  delete?: boolean;
}

const IconButtons = ({ onRestoreClick, onEditClick, onDeleteClick, restore, edit, delete: del }: IconButtonsProps) => (
  <S.IconButtonContainer>
    {restore && (
      <S.IconButtonWrapper onClick={onRestoreClick}>
        <SpinArrowIcon aria-label='카테고리 복구' />
      </S.IconButtonWrapper>
    )}
    {edit && (
      <S.IconButtonWrapper onClick={onEditClick}>
        <PencilIcon width={ICON_SIZE.MEDIUM_LARGE} height={ICON_SIZE.MEDIUM_LARGE} aria-label='카테고리 이름 변경' />
      </S.IconButtonWrapper>
    )}
    {del && (
      <S.IconButtonWrapper onClick={onDeleteClick}>
        <TrashcanIcon width={ICON_SIZE.MEDIUM_LARGE} height={ICON_SIZE.MEDIUM_LARGE} aria-label='카테고리 삭제' />
      </S.IconButtonWrapper>
    )}
  </S.IconButtonContainer>
);

export default IconButtons;
