import { useTranslation } from 'react-i18next';

import { Button, Flex } from '@/components';
import { ConfirmDeleteModal } from '@/pages/MemberTemplatePage/components';

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
}: Props) => {
  const { t } = useTranslation('MemberTemplatePage');

  return (
    <>
      <Flex justify='flex-end'>
        {isEditMode ? (
          <Flex gap='0.25rem'>
            <Button variant='text' size='small' onClick={toggleIsEditMode}>
              {t('templateDeleteSelection.goBack')}
            </Button>
            <Button variant='outlined' size='small' onClick={handleAllSelected}>
              {selectedListLength === templateListLength
                ? t('templateDeleteSelection.deselectAll')
                : t('templateDeleteSelection.selectAll')}
            </Button>
            <Button
              variant={selectedListLength ? 'contained' : 'text'}
              size='small'
              onClick={
                selectedListLength ? toggleDeleteModal : toggleIsEditMode
              }
            >
              {selectedListLength
                ? t('templateDeleteSelection.delete')
                : t('templateDeleteSelection.cancel')}
            </Button>
          </Flex>
        ) : (
          <Button variant='text' size='small' onClick={toggleIsEditMode}>
            {t('templateDeleteSelection.selectDelete')}
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
};

export default TemplateDeleteSelection;
