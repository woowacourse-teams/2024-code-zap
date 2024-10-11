import { useInput, useInputWithValidate, useToggle } from '@/hooks';
import { validateEmail } from '@/service/validates';
import { theme } from '@/style/theme';

import { Button, Input, Modal, Text, Textarea } from '..';
import * as S from './ContactUs.style';

const ContactUs = () => {
  const [isModalOpen, toggleModal] = useToggle();
  const [contents, handleContents] = useInput('');
  const {
    value: email,
    handleChange: handleEmail,
    errorMessage: emailErrorMessage,
  } = useInputWithValidate('', validateEmail);

  const isValidContents = contents.trim().length;

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!isValidContents) {
      return;
    }

    toggleModal();
  };

  return (
    <>
      <S.ContactUSButton onClick={toggleModal}>
        <Text.Medium weight='bold' color={theme.color.light.secondary_800}>
          문의하기
        </Text.Medium>
      </S.ContactUSButton>
      <Modal isOpen={isModalOpen} toggleModal={toggleModal} size='large'>
        <Modal.Header>문의하기</Modal.Header>
        <Modal.Body>
          <S.Form onSubmit={handleSubmit}>
            <Text.Medium as='p' color={theme.color.light.secondary_500}>
              질문/피드백을 편하게 남겨주세요! 여러분의 의견은 더 나은 서비스를 만드는 데 큰 도움이 됩니다. <br />
              답변이 필요하시면 아래 이메일 주소를 남겨주세요. 이메일은 오직 답변을 위해서만 사용됩니다 :)
            </Text.Medium>
            <Textarea id='voc' variant='outlined'>
              <Textarea.Label htmlFor={'voc'}>무엇을 도와드릴까요?</Textarea.Label>
              <Textarea.TextField minRows={5} maxRows={10} value={contents} onChange={handleContents} />
            </Textarea>
            <Input variant='outlined' isValid={!emailErrorMessage}>
              <Input.Label>이메일 (선택)</Input.Label>
              <Input.TextField value={email} onChange={handleEmail} />
              <Input.HelperText>{emailErrorMessage}</Input.HelperText>
            </Input>
          </S.Form>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={toggleModal} variant='outlined'>
            닫기
          </Button>
          <Button disabled={isValidContents ? false : true}>보내기</Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ContactUs;
