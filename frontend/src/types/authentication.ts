export interface SignupRequest {
  email: string;
  nickname: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface CheckEmailResponse {
  check: boolean;
}
