import { passwordEyeIcon } from '@/assets/images';
import { Button, Flex, Input } from '@/components';
import { useShowPassword, useSignupForm } from '@/hooks/authentication';

const SignupPage = () => {
  const { showPassword, handlePasswordToggle } = useShowPassword();
  const { showPassword: showPasswordConfirm, handlePasswordToggle: handlePasswordConfirmToggle } = useShowPassword();

  const {
    email,
    nickname,
    password,
    confirmPassword,
    errors,
    handleEmailChange,
    handleNicknameChange,
    handlePasswordChange,
    handleConfirmPasswordChange,
    isFormValid,
    handleSubmit,
  } = useSignupForm();

  return (
    <>
      <Flex direction='column' justify='center' align='center' height='100vh'>
        <Flex direction='column' justify='center' align='center' gap='2.5rem' width='27.5rem'>
          <h1 style={{ fontSize: '3rem', fontWeight: '700', color: '#F79037' }}>Code-Zap</h1>

          <Input variant='outlined' size='medium'>
            <Input.Label>이메일</Input.Label>
            <Input.TextField type='email' value={email} onChange={handleEmailChange} />
            {errors.email && <Input.HelperText>{errors.email}</Input.HelperText>}
          </Input>

          <Input variant='outlined' size='medium'>
            <Input.Label>닉네임</Input.Label>
            <Input.TextField type='text' value={nickname} onChange={handleNicknameChange} />
            {errors.nickname && <Input.HelperText>{errors.nickname}</Input.HelperText>}
          </Input>

          <Input variant='outlined' size='medium'>
            <Input.Label>비밀번호</Input.Label>
            <Input.TextField
              type={showPassword ? 'text' : 'password'}
              value={password}
              onChange={handlePasswordChange}
            />
            <Input.Adornment>
              <img src={passwordEyeIcon} onClick={handlePasswordToggle} style={{ cursor: 'pointer' }} />
            </Input.Adornment>
            {errors.password && <Input.HelperText>{errors.password}</Input.HelperText>}
          </Input>

          <Input variant='outlined' size='medium'>
            <Input.Label>비밀번호 확인</Input.Label>
            <Input.TextField
              type={showPasswordConfirm ? 'text' : 'password'}
              value={confirmPassword}
              onChange={handleConfirmPasswordChange}
            />
            <Input.Adornment>
              <img src={passwordEyeIcon} onClick={handlePasswordConfirmToggle} style={{ cursor: 'pointer' }} />
            </Input.Adornment>
            {errors.confirmPassword && <Input.HelperText>{errors.confirmPassword}</Input.HelperText>}
          </Input>

          <Button variant='contained' size='filled' disabled={!isFormValid()} onClick={handleSubmit}>
            회원가입
          </Button>
          <Flex justify='flex-end' width='100%' gap='1rem'>
            <span style={{ fontSize: '0.875rem', fontWeight: '400' }}>이미 계정이 있으신가요?</span>
            <Button variant='text' size='small'>
              로그인
            </Button>
          </Flex>
        </Flex>
      </Flex>
    </>
  );
};

export default SignupPage;
