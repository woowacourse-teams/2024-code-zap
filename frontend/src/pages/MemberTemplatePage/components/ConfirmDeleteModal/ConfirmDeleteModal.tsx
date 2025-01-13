import { Button, Modal, Text } from '@/components';

interface Props {
  isDeleteModalOpen: boolean;
  toggleDeleteModal: () => void;
  handleDelete: () => void;
}

const ConfirmDeleteModal = ({ isDeleteModalOpen, toggleDeleteModal, handleDelete }: Props) => (
  <Modal isOpen={isDeleteModalOpen} toggleModal={toggleDeleteModal} size='xsmall'>
    <Modal.Header>
      <Text.Large color='black' weight='bold'>
        정말 삭제하시겠습니까?
      </Text.Large>
    </Modal.Header>
    <Modal.Body>
      <Text.Medium color='black'>삭제된 템플릿은 복구할 수 없습니다.</Text.Medium>
    </Modal.Body>
    <Modal.Footer>
      <Button variant='outlined' onClick={toggleDeleteModal}>
        취소
      </Button>
      <Button onClick={handleDelete}>삭제</Button>
    </Modal.Footer>
  </Modal>
);

export default ConfirmDeleteModal;
