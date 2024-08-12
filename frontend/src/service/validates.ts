export const validateEmail = (email: string) => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  return regex.test(email) && !/\s/.test(email) ? '' : '유효한 이메일을 입력해주세요.';
};

export const validateUsername = (username: string) => {
  const regex = /^[a-zA-Z0-9가-힣-_]+$/;

  return regex.test(username) && username.length > 0 ? '' : '공백을 제외한 1자 이상의 닉네임을 입력해주세요.';
};

export const validatePassword = (password: string) =>
  password.length >= 6 && !/\s/.test(password) ? '' : '공백을 제외한 6자 이상의 비밀번호를 입력해주세요.';

export const validateConfirmPassword = (password: string, confirmPassword: string) =>
  password === confirmPassword ? '' : '비밀번호가 일치하지 않습니다.';
