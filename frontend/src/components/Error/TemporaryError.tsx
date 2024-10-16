import { captureException } from '@sentry/react';
import { FallbackProps } from 'react-error-boundary';

import { Button, Flex, Text } from '@/components';
import { theme } from '@/style/theme';

const TemporaryError = ({ error, resetErrorBoundary }: FallbackProps) => {
  if (error.statusCode !== 500) {
    // statusCode가 500이 아니면 에러를 다시 던져 상위 에러 경계로 전파
    throw error;
  }

  // statusCode가 500인 경우에만 Sentry에 에러를 전송하고 fallback UI를 렌더링
  captureException(error);

  return (
    <Flex direction='column' gap='3rem' margin='2rem 0 0 0' justify='center' align='center' width='100%'>
      <Flex direction='column' gap='2rem' align='center'>
        <Text.XLarge color={theme.color.light.primary_500} weight='bold'>
          잠시 연결이 지연되고 있습니다
        </Text.XLarge>
        <Flex direction='column' justify='center' align='center' gap='1rem'>
          <Text.Medium color={theme.color.light.secondary_600} weight='bold'>
            잠시 후 다시 시도해주세요
          </Text.Medium>
        </Flex>
      </Flex>
      <Button
        weight='bold'
        onClick={() => {
          resetErrorBoundary();
        }}
      >
        <Text.Medium color={theme.color.light.white}>재시도</Text.Medium>
      </Button>
    </Flex>
  );
};

export default TemporaryError;
