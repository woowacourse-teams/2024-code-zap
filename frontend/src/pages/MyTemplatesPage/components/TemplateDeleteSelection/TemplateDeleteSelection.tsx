import { Button, Flex } from '@/components';

import { ConfirmDeleteModal } from '../';

interface Props {
  isEditMode: boolean;
  toggleIsEditMode: () => void;
  isDeleteModalOpen: boolean;
  toggleDeleteModal: () => void;
  handleAllSelected: () => void;
  selectedListLength: number;
  templateListLength: number;
  handleDelete: () => void;
}

const TemplateDeleteSelection = ({
  isEditMode,
  toggleIsEditMode,
  isDeleteModalOpen,
  toggleDeleteModal,
  handleAllSelected,
  selectedListLength,
  templateListLength,
  handleDelete,
}: Props) => (
  <>
    <Flex justify='flex-end'>
      {isEditMode ? (
        <Flex gap='0.25rem'>
          <Button variant='text' size='small' onClick={toggleIsEditMode}>
            돌아가기
          </Button>
          <Button variant='outlined' size='small' onClick={handleAllSelected}>
            {selectedListLength === templateListLength ? '전체 해제' : '전체 선택'}
          </Button>
          <Button
            variant={selectedListLength ? 'contained' : 'text'}
            size='small'
            onClick={selectedListLength ? toggleDeleteModal : toggleIsEditMode}
          >
            {selectedListLength ? '삭제하기' : '취소하기'}
          </Button>
        </Flex>
      ) : (
        <Button variant='text' size='small' onClick={toggleIsEditMode}>
          선택 삭제
        </Button>
      )}
    </Flex>
    {isDeleteModalOpen && (
      <ConfirmDeleteModal
        isDeleteModalOpen={isDeleteModalOpen}
        toggleDeleteModal={toggleDeleteModal}
        handleDelete={handleDelete}
      />
    )}
  </>
);

export default TemplateDeleteSelection;
