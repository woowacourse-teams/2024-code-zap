import { theme } from '@design/style/theme';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';

import { EyeIcon, ZapzapLogo } from '@/assets/images';
import { Button, Flex, Input, Text } from '@/components';
import { useToggle } from '@/hooks';
import { useLoginForm } from '@/pages/LoginPage/hooks';
import { END_POINTS } from '@/routes';
import { useTrackPageViewed } from '@/service/amplitude';

import * as S from './LoginPage.style';

const LoginPage = () => {
  const { t } = useTranslation('LoginPage');

  useTrackPageViewed({ eventName: '[Viewed] 로그인 페이지' });

  const [showPassword, handlePasswordToggle] = useToggle();
  const {
    name,
    password,
    errors,
    handleNameChange,
    handlePasswordChange,
    isFormValid,
    handleSubmit,
  } = useLoginForm();

  return (
    <>
      <S.ResponsiveFlex
        direction='column'
        justify='center'
        align='center'
        height='100vh'
      >
        <S.LoginPageContainer
          direction='column'
          justify='center'
          align='center'
          gap='3.5rem'
          width='27.5rem'
        >
          <Flex direction='column' justify='center' align='center' gap='1rem'>
            <ZapzapLogo width={100} height={100} />
            <S.ResponsiveHeading color={theme.color.light.primary_800}>
              {t('title')}
            </S.ResponsiveHeading>
          </Flex>

          <S.LoginForm
            onSubmit={handleSubmit}
            style={{
              display: 'flex',
              flexDirection: 'column',
              width: '100%',
              height: '100%',
              gap: '1rem',
            }}
          >
            <Input variant='outlined' size='medium' isValid={!errors.name}>
              <Input.Label>{t('form.labels.username')}</Input.Label>
              <Input.TextField
                type='text'
                placeholder={t('form.placeholders.username')}
                placeholderColor='transparent'
                value={name}
                onChange={handleNameChange}
                autoComplete='username'
              />
              <Input.HelperText>{errors.name}</Input.HelperText>
            </Input>

            <Input variant='outlined' size='medium' isValid={!errors.password}>
              <Input.Label>{t('form.labels.password')}</Input.Label>
              <Input.TextField
                type={showPassword ? 'text' : 'password'}
                placeholder={t('form.placeholders.password')}
                placeholderColor='transparent'
                value={password}
                onChange={handlePasswordChange}
                autoComplete='current-password'
              />
              <Input.Adornment
                as='button'
                aria-label={t('form.aria.showPassword')}
                onClick={handlePasswordToggle}
              >
                <EyeIcon aria-hidden />
              </Input.Adornment>
              <Input.HelperText>{errors.password}</Input.HelperText>
            </Input>

            <Button
              type='submit'
              variant='contained'
              fullWidth
              disabled={!isFormValid()}
            >
              {t('form.buttons.login')}
            </Button>
            <Flex justify='flex-end' align='center' width='100%' gap='0.5rem'>
              <Text.XSmall color={theme.color.light.secondary_600}>
                {t('signup.question')}
              </Text.XSmall>

              <Link to={END_POINTS.SIGNUP}>
                <Button variant='text' size='small'>
                  {t('form.buttons.signup')}
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
