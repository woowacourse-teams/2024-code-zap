import { Link } from 'react-router-dom';

import { passwordEyeIcon } from '@/assets/images';
import { Button, Flex, Heading, Input, Text } from '@/components';
import { useSignupForm } from '@/hooks/authentication';
import { useToggle } from '@/hooks/utils';

const SignupPage = () => {
  const [showPassword, handlePasswordToggle] = useToggle();
  const [showPasswordConfirm, handlePasswordConfirmToggle] = useToggle();

  const {
    name,
    password,
    confirmPassword,
    errors,
    handleNameChange,
    handlePasswordChange,
    handleConfirmPasswordChange,
    handleNameCheck,
    isFormValid,
    handleSubmit,
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
            <Input variant='outlined' size='medium' isValid={!errors.name}>
              <Input.Label>아이디</Input.Label>
              <Input.TextField
                type='text'
                value={name}
                onChange={handleNameChange}
                onBlur={() => handleNameCheck()}
                autoComplete='username'
              />
              <Input.HelperText>{errors.name}</Input.HelperText>
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

            <Flex justify='flex-end' align='center' width='100%' gap='0.5rem'>
              <Text.XSmall color='#6B7079'>이미 계정이 있으신가요?</Text.XSmall>
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
