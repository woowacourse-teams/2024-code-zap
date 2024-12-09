import { captureException } from '@sentry/react';

import { ApiError } from '@/api/Error';
import { TigerLogo } from '@/assets/images';
import { Button, Flex, Text } from '@/components';
import { useCustomNavigate } from '@/hooks';
import { useTrackPageViewed } from '@/service/amplitude';
import { theme } from '@/style/theme';

interface props {
  resetError?: () => void;
  error?: ApiError;
}

const ForbiddenPage = ({ resetError, error }: props) => {
  const navigate = useCustomNavigate();

  captureException(error);
  useTrackPageViewed({ eventName: '[Viewed] 403 Forbidden 페이지' });

  return (
    <Flex direction='column' gap='3rem' margin='2rem 0 0 0' justify='center' align='center'>
      <TigerLogo aria-label='호랑이 로고' />
      <Flex direction='column' gap='2rem' align='center'>
        <Text.XLarge color={theme.color.light.primary_500} weight='bold'>
          해당 페이지에 접근할 수 있는 권한이 없습니다.
        </Text.XLarge>
        <Flex direction='column' justify='center' align='center' gap='1rem'>
          <Text.Medium color={theme.color.light.secondary_600} weight='bold'>
            입력하신 주소가 정확한지 다시 확인하거나 홈으로 이동해주세요.
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
        <Text.Medium color={theme.color.light.white}>홈으로 이동</Text.Medium>
      </Button>
    </Flex>
  );
};

export default ForbiddenPage;
