import { Text, Modal, Flex, Button } from '@/components';
import type { Category } from '@/types';

import CategoryItems from './CategoryItems';
import { useCategoryEditModal } from '../../hooks';
import * as S from './CategoryEditModal.style';

interface CategoryEditModalProps {
  isOpen: boolean;
  toggleModal: () => void;
  categoryList: Category[];
  onDeleteCategory: (deletedIds: number[]) => void;
}

const CategoryEditModal = ({ isOpen, toggleModal, categoryList, onDeleteCategory }: CategoryEditModalProps) => {
  const {
    editedCategoryList,
    deleteCategoryIds,
    editingCategoryId,
    invalidIds,
    isValid,
    isNewCategory,
    handleNameInputChange,
    handleOrdinalChange,
    handleDeleteClick,
    handleRestoreClick,
    handleEditClick,
    handleNameInputBlur,
    handleAddCategory,
    handleSaveChanges,
    handleCancelEditWithReset,
  } = useCategoryEditModal({ categoryList, toggleModal, onDeleteCategory });

  return (
    <Modal isOpen={isOpen} toggleModal={handleCancelEditWithReset} size='small'>
      <Modal.Header>{'카테고리 편집'}</Modal.Header>
      <Modal.Body>
        <S.EditCategoryItemList>
          <CategoryItems
            editedCategoryList={editedCategoryList}
            deleteCategoryIds={deleteCategoryIds}
            editingCategoryId={editingCategoryId}
            invalidIds={invalidIds}
            handleOrdinalChange={handleOrdinalChange}
            isNewCategory={isNewCategory}
            onEditClick={handleEditClick}
            onDeleteClick={handleDeleteClick}
            onRestoreClick={handleRestoreClick}
            onNameInputChange={handleNameInputChange}
            onNameInputBlur={handleNameInputBlur}
          />
          <S.EditCategoryItem isButton={true} disabled={!isValid}>
            <Button fullWidth variant='text' hoverStyle='none' onClick={handleAddCategory} disabled={!isValid}>
              {'+ 카테고리 추가'}
            </Button>
          </S.EditCategoryItem>
        </S.EditCategoryItemList>
      </Modal.Body>
      <Modal.Footer>
        <Flex direction='column' gap='0.75rem' width='100%' style={{ alignSelf: 'flex-end' }}>
          <Flex height='1em'>
            {invalidIds.length > 0 && (
              <Text.Small color={'red'}>{'유효하지 않은 카테고리 이름이 있습니다.'}</Text.Small>
            )}
          </Flex>
          <Flex justify='flex-end' gap='1rem'>
            <Button variant='outlined' onClick={handleCancelEditWithReset}>
              {'취소'}
            </Button>
            <Button onClick={handleSaveChanges} disabled={!isValid}>
              {'저장'}
            </Button>
          </Flex>
        </Flex>
      </Modal.Footer>
    </Modal>
  );
};

export default CategoryEditModal;
