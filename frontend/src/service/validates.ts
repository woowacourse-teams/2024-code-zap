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
