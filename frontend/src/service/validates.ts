import { t } from 'i18next';

import { SourceCodes } from '@/types';
import { getByteSize } from '@/utils';

export const validateName = (name: string) => {
  const MAX_LENGTH = 255;
  const MIN_LENGTH = 1;
  const regex = /^[a-zA-Z0-9가-힣-_]+$/;

  return regex.test(name) &&
    name.length >= MIN_LENGTH &&
    name.length <= MAX_LENGTH
    ? ''
    : t('Validates:name.invalid', {
        min: MIN_LENGTH,
        max: MAX_LENGTH,
      });
};

export const validatePassword = (password: string) => {
  const MAX_LENGTH = 16;
  const MIN_LENGTH = 8;
  const hasLetters = /[a-zA-Z]/.test(password);
  const hasNumbers = /[0-9]/.test(password);
  const hasNoSpaces = !/\s/.test(password);
  const isValidLength =
    password.length >= MIN_LENGTH && password.length <= MAX_LENGTH;

  return hasLetters && hasNumbers && isValidLength && hasNoSpaces
    ? ''
    : t('Validates:password.invalid', {
        min: MIN_LENGTH,
        max: MAX_LENGTH,
      });
};

export const validateConfirmPassword = (
  password: string,
  confirmPassword: string,
) => (password === confirmPassword ? '' : t('Validates:password.mismatch'));

export const validateFilename = (filename: string) => {
  const MAX_LENGTH = 255;
  const invalidChars = /[<>:"/\\|?*]/;

  if (filename.length > MAX_LENGTH) {
    return t('Validates:filename.tooLong', { max: MAX_LENGTH });
  }

  if (invalidChars.test(filename)) {
    return t('Validates:filename.invalidChars');
  }

  return '';
};

export const validateSourceCode = (sourceCode: string) => {
  const MAX_CONTENT_SIZE = 65535;
  const currentByteSize = getByteSize(sourceCode);

  if (currentByteSize > MAX_CONTENT_SIZE) {
    return t('Validates:sourceCode.tooLong', { max: MAX_CONTENT_SIZE });
  }

  return '';
};

export const validateCategoryName = (categoryName: string) => {
  const maxLength = 15;

  if (categoryName.trim().length > maxLength) {
    return t('Validates:category.invalid', { max: maxLength });
  }

  return '';
};

export const validateTagLength = (tag: string) => {
  const MAX_LENGTH = 30;

  if (tag.length > MAX_LENGTH) {
    return t('Validates:tag.tooLong', { max: MAX_LENGTH });
  }

  return '';
};

export const validateEmail = (email: string) => {
  const emailRegex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;

  const isValid = emailRegex.test(email);

  if (!email.length) {
    return '';
  }

  if (!isValid) {
    return t('Validates:email.invalid');
  }

  return '';
};

export const validateTemplate = (title: string, sourceCodes: SourceCodes[]) => {
  if (!title) {
    return t('Validates:template.titleRequired');
  }

  if (
    sourceCodes.filter(({ content }) => !content || content.trim() === '')
      .length
  ) {
    return t('Validates:template.sourceCodeRequired');
  }

  return '';
};
