import { useState } from 'react';

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

  const { successAlert } = useToast();

  const isValidContents = message.trim().length !== 0;

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

    const res = await sendData();

    if (res) {
      successSubmit();
      successAlert('보내기 완료! 소중한 의견 감사합니다:)');
    }
  };

  const sendData = () => {
    const URL = process.env.GOOGLE_URL || '';

    return fetch(URL, {
      method: 'POST',
      mode: 'no-cors',
      body: JSON.stringify({ message, email, name, memberId }),
      headers: {
        'Content-Type': 'application/json',
      },
    });
  };

  const successSubmit = () => {
    setIsSending(false);
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
              <Textarea.Label htmlFor={'voc'}>무엇을 도와드릴까요?</Textarea.Label>
              <Textarea.TextField
                minRows={5}
                maxRows={10}
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
