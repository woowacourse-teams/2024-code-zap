export const validateName = (name: string) => {
  const regex = /^[a-zA-Z0-9가-힣-_]+$/;

  return regex.test(name) && name.length > 0 ? '' : '1자 이상의 아이디를 입력해주세요.';
};

export const validatePassword = (password: string) => {
  const hasLetters = /[a-zA-Z]/.test(password);
  const hasNumbers = /[0-9]/.test(password);
  const hasNoSpaces = !/\s/.test(password);
  const isValidLength = password.length >= 8;

  return hasLetters && hasNumbers && isValidLength && hasNoSpaces
    ? ''
    : '영문자, 숫자를 포함한 8자 이상의 비밀번호를 입력해주세요.';
};

export const validateConfirmPassword = (password: string, confirmPassword: string) =>
  password === confirmPassword ? '' : '비밀번호가 일치하지 않습니다.';

export const validateFileName = (fileName: string) => {
  const fileNamePattern = /^[가-힣a-zA-Z0-9_-]+\.[a-zA-Z]+$/;

  const invalidChars = /[<>:"/\\|?*]/;

  const maxLength = 255;

  if (fileName.length > maxLength || fileName.length === 0) {
    return '파일명의 길이는 1~255자로 입력해주세요!';
  }

  if (invalidChars.test(fileName)) {
    return '특수 문자 (<, >, :, ", /, , |, ?, *)는 사용할 수 없습니다!';
  }

  if (!fileNamePattern.test(fileName)) {
    return '확장자를 입력해주세요!';
  }

  return '';
};

export const validateCategoryName = (categoryName: string) => {
  const maxLength = 15;

  if (categoryName.trim().length > maxLength) {
    return `카테고리 이름은 ${maxLength}자 이내로 입력해주세요.`;
  }

  if (categoryName.trim().length === 0) {
    return '카테고리 이름을 입력해주세요.';
  }

  return '';
};
