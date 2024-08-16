export interface SignupRequest {
  name: string;
  password: string;
}

export interface LoginRequest {
  name: string;
  password: string;
}

export interface MemberInfo {
  memberId: number | undefined;
  name: string | undefined;
}
