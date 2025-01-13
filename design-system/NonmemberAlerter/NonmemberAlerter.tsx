import { Button, Flex, Modal, Text } from '@/components';
import { useCustomNavigate } from '@/hooks';
import { END_POINTS } from '@/routes';
import { theme } from '@/style/theme';

interface Props {
  isOpen: boolean;
  content?: string;
  toggleModal: () => void;
}

const NonmemberAlerter = ({ isOpen, content, toggleModal }: Props) => {
  const navigate = useCustomNavigate();

  const handleLoginButtonClick = () => {
    navigate(END_POINTS.LOGIN);
  };

  return (
    <Modal isOpen={isOpen} toggleModal={toggleModal} size='xsmall'>
      <Modal.Body>
        <Text.Medium as='p' color={theme.color.light.secondary_900}>
          {content || (
            <>
              {'회원만 사용할 수 있는 기능입니다.'}
              <br />
              {'로그인하시겠습니까?'}
            </>
          )}
        </Text.Medium>
      </Modal.Body>
      <Modal.Footer>
        <Flex width='100%' justify='center' gap='1rem'>
          <Button variant='outlined' onClick={toggleModal}>
            {'취소'}
          </Button>
          <Button onClick={handleLoginButtonClick}>{'로그인'}</Button>
        </Flex>
      </Modal.Footer>
    </Modal>
  );
};

export default NonmemberAlerter;
