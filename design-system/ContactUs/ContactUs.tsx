import {
  Button,
  Input,
  LoadingBall,
  Modal,
  Text,
  Textarea,
} from '@/components';
import { useInput, useInputWithValidate, useToggle, useToast } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { validateEmail } from '@/service/validates';
import { theme } from '@/style/theme';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';

import * as S from './ContactUs.style';

const ContactUs = () => {
  const { t } = useTranslation('ContactUs');
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

  const handleSubmit = (
    e: React.FormEvent<HTMLFormElement> | React.MouseEvent<HTMLButtonElement>,
  ) => {
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
      successAlert(t('alerts.successMessage'));
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
          {t('button.contact')}
        </Text.Medium>
      </S.ContactUSButton>

      <Modal isOpen={isModalOpen} toggleModal={toggleModal} size='large'>
        <Modal.Header>{t('modal.title')}</Modal.Header>
        <Modal.Body>
          <S.Form onSubmit={handleSubmit}>
            <Text.Medium as='p' color={theme.color.light.secondary_500}>
              {t('modal.description')} <br />
              {t('modal.emailImageInfo')}
            </Text.Medium>
            <Textarea id='voc' variant='outlined'>
              <Textarea.Label htmlFor={'voc'}>
                {t('modal.messageLabel')}
              </Textarea.Label>
              <Textarea.TextField
                minRows={5}
                maxRows={10}
                value={message}
                onChange={handleMessage}
                disabled={isSending}
              />
            </Textarea>
            <Text.Medium as='p' color={theme.color.light.secondary_500}>
              {t('modal.emailDescription')}
            </Text.Medium>
            <Input variant='outlined' isValid={!emailErrorMessage}>
              <Input.Label>{t('modal.emailLabel')}</Input.Label>
              <Input.TextField
                value={email}
                onChange={handleEmail}
                disabled={isSending}
              />
              <Input.HelperText>{emailErrorMessage}</Input.HelperText>
            </Input>
          </S.Form>
        </Modal.Body>
        <Modal.Footer>
          <Button onClick={toggleModal} variant='outlined'>
            {t('buttons.close')}
          </Button>
          {isSending ? (
            <S.LoadingContainer>
              <LoadingBall />
            </S.LoadingContainer>
          ) : (
            <Button
              disabled={isValidContents && !emailErrorMessage ? false : true}
              onClick={handleSubmit}
            >
              {t('buttons.send')}
            </Button>
          )}
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ContactUs;
