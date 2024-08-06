import { Link } from 'react-router-dom';

import { passwordEyeIcon } from '@/assets/images';
import { Button, Flex, Heading, Input, Text } from '@/components';
import { useShowPassword, useSignupForm } from '@/hooks/authentication';

const SignupPage = () => {
  const { showPassword, handlePasswordToggle } = useShowPassword();
  const { showPassword: showPasswordConfirm, handlePasswordToggle: handlePasswordConfirmToggle } = useShowPassword();

  const {
    email,
    username,
    password,
    confirmPassword,
    errors,
    handleEmailChange,
    handleUsernameChange,
    handlePasswordChange,
    handleConfirmPasswordChange,
    isFormValid,
    handleSubmit,
    handleEmailCheck,
    handleUsernameCheck,
  } = useSignupForm();

  return (
    <>
      <Flex direction='column' justify='center' align='center' height='100vh'>
        <Flex direction='column' justify='center' align='center' width='27.5rem' gap='3.5rem'>
          <Heading.XLarge color='#F79037'>Hello Codezap</Heading.XLarge>

          <form
            onSubmit={handleSubmit}
            style={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', gap: '1rem' }}
          >
            <Input variant='outlined' size='medium' isValid={!errors.email}>
              <Input.Label>이메일</Input.Label>
              <Input.TextField
                type='email'
                value={email}
                onChange={handleEmailChange}
                onBlur={() => handleEmailCheck()}
                autoComplete='email'
              />
              <Input.HelperText>{errors.email}</Input.HelperText>
            </Input>

            <Input variant='outlined' size='medium' isValid={!errors.username}>
              <Input.Label>닉네임</Input.Label>
              <Input.TextField
                type='text'
                value={username}
                onChange={handleUsernameChange}
                onBlur={() => handleUsernameCheck()}
                autoComplete='username'
              />
              <Input.HelperText>{errors.username}</Input.HelperText>
            </Input>

            <Input variant='outlined' size='medium' isValid={!errors.password}>
              <Input.Label>비밀번호</Input.Label>
              <Input.TextField
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={handlePasswordChange}
                autoComplete='new-password'
              />
              <Input.Adornment>
                <img src={passwordEyeIcon} onClick={handlePasswordToggle} style={{ cursor: 'pointer' }} />
              </Input.Adornment>
              <Input.HelperText>{errors.password}</Input.HelperText>
            </Input>

            <Input variant='outlined' size='medium' isValid={!errors.confirmPassword}>
              <Input.Label>비밀번호 확인</Input.Label>
              <Input.TextField
                type={showPasswordConfirm ? 'text' : 'password'}
                value={confirmPassword}
                onChange={handleConfirmPasswordChange}
                autoComplete='new-password'
              />
              <Input.Adornment>
                <img src={passwordEyeIcon} onClick={handlePasswordConfirmToggle} style={{ cursor: 'pointer' }} />
              </Input.Adornment>
              <Input.HelperText>{errors.confirmPassword}</Input.HelperText>
            </Input>

            <Button type='submit' variant='contained' fullWidth disabled={!isFormValid()}>
              회원가입
            </Button>

            <Flex justify='flex-end' width='100%' gap='1rem'>
              <Text.Small color='#6B7079'>이미 계정이 있으신가요?</Text.Small>
              <Link to={'/login'}>
                <Button variant='text' size='small'>
                  로그인
                </Button>
              </Link>
            </Flex>
          </form>
        </Flex>
      </Flex>
    </>
  );
};

export default SignupPage;
