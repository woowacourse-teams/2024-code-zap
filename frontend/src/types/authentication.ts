export interface SignupRequest {
  email: string;
  nickname: string;
  password: string;
  confirmPassword: string;
}

export interface CheckEmailResponse {
  check: boolean;
}
