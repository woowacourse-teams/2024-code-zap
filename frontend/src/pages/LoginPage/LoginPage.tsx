import { Link } from 'react-router-dom';

import { EyeIcon, ZapzapLogo } from '@/assets/images';
import { Button, Flex, Input, Text } from '@/components';
import { useToggle } from '@/hooks';
import { END_POINTS } from '@/routes';
import { useTrackPageViewed } from '@/service/amplitude';

import { useLoginForm } from './hooks';
import * as S from './LoginPage.style';
import { theme } from '@/style/theme';

const LoginPage = () => {
  useTrackPageViewed({ eventName: '[Viewed] 로그인 페이지' });

  const [showPassword, handlePasswordToggle] = useToggle();
  const { name, password, errors, handleNameChange, handlePasswordChange, isFormValid, handleSubmit } = useLoginForm();

  return (
    <>
      <S.ResponsiveFlex direction='column' justify='center' align='center' height='100vh'>
        <S.LoginPageContainer direction='column' justify='center' align='center' gap='3.5rem' width='27.5rem'>
          <Flex direction='column' justify='center' align='center' gap='1rem'>
            <ZapzapLogo width={100} height={100} />
            <S.ResponsiveHeading color={theme.color.light.primary_800}>환영하잽</S.ResponsiveHeading>
          </Flex>

          <S.LoginForm
            onSubmit={handleSubmit}
            style={{ display: 'flex', flexDirection: 'column', width: '100%', height: '100%', gap: '1rem' }}
          >
            <Input variant='outlined' size='medium' isValid={!errors.name}>
              <Input.Label>아이디 (닉네임)</Input.Label>
              <Input.TextField
                type='text'
                placeholder='아이디 입력. 1자에서 255자의 올바른 문자를 입력해주세요'
                placeholderColor='transparent'
                value={name}
                onChange={handleNameChange}
                autoComplete='username'
              />
              <Input.HelperText>{errors.name}</Input.HelperText>
            </Input>

            <Input variant='outlined' size='medium' isValid={!errors.password}>
              <Input.Label>비밀번호</Input.Label>
              <Input.TextField
                type={showPassword ? 'text' : 'password'}
                placeholder='비밀번호 입력. 영문자, 숫자를 포함한 8자에서 16자의 비밀번호를 입력해주세요.'
                placeholderColor='transparent'
                value={password}
                onChange={handlePasswordChange}
                autoComplete='current-password'
              />
              <Input.Adornment as='button' aria-label='비밀번호 보기' onClick={handlePasswordToggle}>
                <EyeIcon aria-hidden />
              </Input.Adornment>
              <Input.HelperText>{errors.password}</Input.HelperText>
            </Input>

            <Button type='submit' variant='contained' fullWidth disabled={!isFormValid()}>
              로그인
            </Button>
            <Flex justify='flex-end' align='center' width='100%' gap='0.5rem'>
              <Text.XSmall color={theme.color.light.secondary_600}>계정이 없으신가요?</Text.XSmall>

              <Link to={END_POINTS.SIGNUP}>
                <Button variant='text' size='small'>
                  회원가입
                </Button>
              </Link>
            </Flex>
          </S.LoginForm>
        </S.LoginPageContainer>
      </S.ResponsiveFlex>
    </>
  );
};

export default LoginPage;
