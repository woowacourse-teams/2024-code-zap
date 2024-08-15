import { Link } from 'react-router-dom';

import { passwordEyeIcon } from '@/assets/images';
import { Button, Flex, Heading, Input, Text } from '@/components';
import { useLoginForm } from '@/hooks/authentication/useLoginForm';
import { useToggle } from '@/hooks/utils';

const LoginPage = () => {
  const [showPassword, handlePasswordToggle] = useToggle();
  const { email, password, errors, handleEmailChange, handlePasswordChange, isFormValid, handleSubmit } =
    useLoginForm();

  return (
    <>
      <Flex direction='column' justify='center' align='center' height='100vh'>
        <Flex direction='column' justify='center' align='center' gap='3.5rem' width='27.5rem'>
          <Heading.XLarge color='#F79037'>Hello Codezap</Heading.XLarge>

          <form
            onSubmit={handleSubmit}
            style={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', gap: '1rem' }}
          >
            <Input variant='outlined' size='medium' isValid={!errors.email}>
              <Input.Label>이메일</Input.Label>
              <Input.TextField type='email' value={email} onChange={handleEmailChange} autoComplete='email' />
              <Input.HelperText>{errors.email}</Input.HelperText>
            </Input>

            <Input variant='outlined' size='medium' isValid={!errors.password}>
              <Input.Label>비밀번호</Input.Label>
              <Input.TextField
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={handlePasswordChange}
                autoComplete='current-password'
              />
              <Input.Adornment>
                <img src={passwordEyeIcon} onClick={handlePasswordToggle} style={{ cursor: 'pointer' }} />
              </Input.Adornment>
              <Input.HelperText>{errors.password}</Input.HelperText>
            </Input>

            <Button type='submit' variant='contained' fullWidth disabled={!isFormValid()}>
              로그인
            </Button>
            <Flex justify='flex-end' align='center' width='100%' gap='0.5rem'>
              <Text.XSmall color='#6B7079'>계정이 없으신가요?</Text.XSmall>

              <Link to={'/signup'}>
                <Button variant='text' size='small'>
                  회원가입
                </Button>
              </Link>
            </Flex>
          </form>
        </Flex>
      </Flex>
    </>
  );
};

export default LoginPage;
