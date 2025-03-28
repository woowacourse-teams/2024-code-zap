import { theme } from '@design/style/theme';
import { useTranslation } from 'react-i18next';

import { TigerLogo } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { useCustomNavigate } from '@/hooks';

interface props {
  resetError?: () => void;
}

const NotFoundPage = ({ resetError }: props) => {
  const navigate = useCustomNavigate();
  const { t } = useTranslation('NotFoundPage');

  return (
    <Flex
      direction='column'
      gap='3rem'
      margin='2rem 0 0 0'
      justify='center'
      align='center'
    >
      <TigerLogo aria-label={t('aria.tigerLogo')} />
      <Heading.XLarge color={theme.color.light.primary_500}>
        {t('error')}
      </Heading.XLarge>
      <Flex direction='column' gap='2rem' align='center'>
        <Text.XLarge color={theme.color.light.primary_500} weight='bold'>
          {t('mainMessage')}
        </Text.XLarge>
        <Flex direction='column' justify='center' align='center' gap='1rem'>
          <Text.Medium color={theme.color.light.secondary_600} weight='bold'>
            {t('subMessages.notExist')}
          </Text.Medium>
          <Text.Medium color={theme.color.light.secondary_600} weight='bold'>
            {t('subMessages.checkAddress')}
          </Text.Medium>
        </Flex>
      </Flex>
      <Button
        weight='bold'
        onClick={() => {
          resetError && resetError();
          navigate('/');
        }}
      >
        <Text.Medium color={theme.color.light.white}>
          {t('button.home')}
        </Text.Medium>
      </Button>
    </Flex>
  );
};

export default NotFoundPage;
