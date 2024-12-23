import { useState } from 'react';

import { postContact } from '@/api/contact';
import { Button, Input, LoadingBall, Modal, Text, Textarea } from '@/components';
import { useInput, useInputWithValidate, useToggle, useToast } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { validateEmail } from '@/service/validates';
import { theme } from '@/style/theme';

import * as S from './ContactUs.style';

const ContactUs = () => {
  const [isModalOpen, toggleModal] = useToggle();
  const [isSending, setIsSending] = useState(false);
  const [message, handleMessage, resetMessage] = useInput('');
  const {
    value: email,
    handleChange: handleEmail,
    resetValue: resetEmail,
    errorMessage: emailErrorMessage,
  } = useInputWithValidate('', validateEmail);

  const {
    memberInfo: { name, memberId },
  } = useAuth();

  const { successAlert, failAlert } = useToast();

  const MIN_CONTENTS_LENGTH = 10;
  const MAX_CONTENTS_LENGTH = 10000;
  const isValidContents = message.trim().length >= MIN_CONTENTS_LENGTH && message.length <= MAX_CONTENTS_LENGTH;

  const handleSubmit = (e: React.FormEvent<HTMLFormElement> | React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    if (!isValidContents || emailErrorMessage) {
      return;
    }

    submitForm();
  };

  const submitForm = async () => {
    setIsSending(true);
    toggleModal();

    const contactBody = { message, email: email || null, name: name || null, memberId: memberId || null };

    try {
      await postContact(contactBody);
      successSubmit();
    } catch {
      failAlert('문의 보내기에 실패하였습니다. 다시 시도하시거나 이메일로 직접 문의해주세요');
    } finally {
      setIsSending(false);
    }
  };

  const successSubmit = () => {
    successAlert('보내기 완료! 소중한 의견 감사합니다:)');
    resetForm();
  };

  const resetForm = () => {
    resetEmail();
    resetMessage();
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
              이미지 등을 함께 보내실 경우 codezap2024@gmail.com으로 직접 이메일을 보내실 수 있습니다.
            </Text.Medium>
            <Textarea id='voc' variant='outlined'>
              <Textarea.Label htmlFor={'voc'}>무엇을 도와드릴까요? (10글자 이상 작성해주세요)</Textarea.Label>
              <Textarea.TextField
                minRows={5}
                maxRows={10}
                maxLength={MAX_CONTENTS_LENGTH}
                value={message}
                onChange={handleMessage}
                disabled={isSending}
              />
            </Textarea>
            <Text.Medium as='p' color={theme.color.light.secondary_500}>
              답변이 필요하시면 아래 이메일 주소를 남겨주세요. 이메일은 오직 답변을 위해서만 사용됩니다 :)
            </Text.Medium>
            <Input variant='outlined' isValid={!emailErrorMessage}>
              <Input.Label>이메일 (선택)</Input.Label>
              <Input.TextField value={email} onChange={handleEmail} disabled={isSending} />
              <Input.HelperText>{emailErrorMessage}</Input.HelperText>
            </Input>
          </S.Form>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={toggleModal} variant='outlined'>
            닫기
          </Button>
          {isSending ? (
            <S.LoadingContainer>
              <LoadingBall />
            </S.LoadingContainer>
          ) : (
            <Button disabled={isValidContents && !emailErrorMessage ? false : true} onClick={handleSubmit}>
              보내기
            </Button>
          )}
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ContactUs;
