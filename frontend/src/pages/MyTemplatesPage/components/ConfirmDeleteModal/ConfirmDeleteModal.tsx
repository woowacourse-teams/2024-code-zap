import { Button, Flex, Modal, Text } from '@/components';

interface ConfirmDeleteModalProps {
  isDeleteModalOpen: boolean;
  toggleDeleteModal: () => void;
  handleDelete: () => void;
}

const ConfirmDeleteModal = ({ isDeleteModalOpen, toggleDeleteModal, handleDelete }: ConfirmDeleteModalProps) => (
  <Modal isOpen={isDeleteModalOpen} toggleModal={toggleDeleteModal} size='xsmall'>
    <Flex direction='column' justify='space-between' align='center' margin='1rem 0 0 0' gap='2rem'>
      <Flex direction='column' justify='center' align='center' gap='0.75rem'>
        <Text.Large color='black' weight='bold'>
          정말 삭제하시겠습니까?
        </Text.Large>
        <Text.Medium color='black'>삭제된 템플릿은 복구할 수 없습니다.</Text.Medium>
      </Flex>
      <Flex justify='center' align='center' gap='0.5rem'>
        <Button variant='outlined' onClick={toggleDeleteModal}>
          취소
        </Button>
        <Button onClick={handleDelete}>삭제</Button>
      </Flex>
    </Flex>
  </Modal>
);

export default ConfirmDeleteModal;
