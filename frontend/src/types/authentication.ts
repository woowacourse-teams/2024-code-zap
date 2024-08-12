export interface SignupRequest {
  email: string;
  username: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface CheckUsernameResponse {
  check: boolean;
}

export interface MemberInfo {
  memberId: number | undefined;
  username: string | undefined;
}
