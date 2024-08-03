export interface SignupRequest {
  email: string;
  username: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface CheckEmailResponse {
  check: boolean;
}

export interface CheckUsernameResponse {
  check: boolean;
}
