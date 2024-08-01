import { Link } from 'react-router-dom';

import { passwordEyeIcon } from '@/assets/images';
import { Button, Flex, Input } from '@/components';
import { useShowPassword } from '@/hooks/authentication';
import { useLoginForm } from '@/hooks/authentication/useLoginForm';

const LoginPage = () => {
  const { showPassword, handlePasswordToggle } = useShowPassword();
  const { email, password, errors, handleEmailChange, handlePasswordChange, isFormValid, handleSubmit } =
    useLoginForm();

  return (
    <>
      <Flex direction='column' justify='center' align='center' height='100vh'>
        <Flex direction='column' justify='center' align='center' gap='2.5rem' width='27.5rem'>
          <h1 style={{ fontSize: '3rem', fontWeight: '700', color: '#F79037' }}>Code-Zap</h1>

          <Input variant='outlined' size='medium' isValid={!errors.email}>
            <Input.Label>이메일</Input.Label>
            <Input.TextField type='email' value={email} onChange={handleEmailChange} />
            <Input.HelperText>{errors.email}</Input.HelperText>
          </Input>

          <Input variant='outlined' size='medium' isValid={!errors.password}>
            <Input.Label>비밀번호</Input.Label>
            <Input.TextField
              type={showPassword ? 'text' : 'password'}
              value={password}
              onChange={handlePasswordChange}
            />
            <Input.Adornment>
              <img src={passwordEyeIcon} onClick={handlePasswordToggle} style={{ cursor: 'pointer' }} />
            </Input.Adornment>
            <Input.HelperText>{errors.password}</Input.HelperText>
          </Input>

          <Button variant='contained' size='filled' disabled={!isFormValid()} onClick={handleSubmit}>
            로그인
          </Button>
          <Flex justify='flex-end' width='100%' gap='1rem'>
            <span style={{ fontSize: '0.875rem', fontWeight: '400' }}>계정이 없으신가요?</span>

            <Link to={'/signup'}>
              <Button variant='text' size='small'>
                회원가입
              </Button>
            </Link>
          </Flex>
        </Flex>
      </Flex>
    </>
  );
};

export default LoginPage;
