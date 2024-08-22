import { Link } from 'react-router-dom';

import { EyeIcon } from '@/assets/images';
import { Button, Flex, Input, Text } from '@/components';
import { useSignupForm } from '@/hooks/authentication';
import { useToggle } from '@/hooks/utils';
import * as S from './SignupPage.style';

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
      <S.ResponsiveFlex direction='column' justify='center' align='center' height='100vh'>
        <S.SignupPageContainer direction='column' justify='center' align='center' width='27.5rem' gap='3.5rem'>
          <Flex direction='column' justify='center' align='center' gap='1rem'>
            <S.ResponsiveHeading color='#F79037'>환영하잽</S.ResponsiveHeading>
          </Flex>

          <S.SignupForm
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
                <EyeIcon onClick={handlePasswordToggle} css={{ cursor: 'pointer' }} aria-label='비밀번호 보기' />
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
                <EyeIcon
                  onClick={handlePasswordConfirmToggle}
                  css={{ cursor: 'pointer' }}
                  aria-label='비밀번호확인 보기'
                />
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
          </S.SignupForm>
        </S.SignupPageContainer>
      </S.ResponsiveFlex>
    </>
  );
};

export default SignupPage;
