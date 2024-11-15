import { Link } from 'react-router-dom';

import { EyeIcon, ZapzapLogo } from '@/assets/images';
import { Button, Flex, Input, Text } from '@/components';
import { useToggle } from '@/hooks';
import { useTrackPageViewed } from '@/service/amplitude';

import { useSignupForm } from './hooks';
import * as S from './SignupPage.style';
import { theme } from '@/style/theme';

const SignupPage = () => {
  useTrackPageViewed({ eventName: '[Viewed] 회원가입 페이지' });

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
            <ZapzapLogo width={100} height={100} />
            <S.ResponsiveHeading color={theme.color.light.primary_800}>환영하잽</S.ResponsiveHeading>
          </Flex>

          <S.SignupForm
            onSubmit={handleSubmit}
            style={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', gap: '1rem' }}
          >
            <Input variant='outlined' size='medium' isValid={!errors.name}>
              <Input.Label>아이디 (닉네임)</Input.Label>
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
              <Text.XSmall color={theme.color.light.secondary_600}>이미 계정이 있으신가요?</Text.XSmall>
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
