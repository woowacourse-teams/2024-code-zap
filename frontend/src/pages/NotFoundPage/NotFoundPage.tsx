import { useNavigate } from 'react-router-dom';

import { tigger } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { theme } from '@/style/theme';

const ErrorPage = () => {
  const navigate = useNavigate();

  return (
    <Flex direction='column' gap='3rem' margin='2rem 0 0 0' justify='center' align='center'>
      <img src={tigger} alt='' height={340} />
      <Heading.XLarge color={theme.color.light.primary_500}>404 ERROR</Heading.XLarge>
      <Flex direction='column' gap='2rem' align='center'>
        <Text.XLarge color={theme.color.light.primary_500} weight='bold'>
          죄송합니다. 요청하신 페이지를 찾을 수 없습니다.
        </Text.XLarge>
        <Flex direction='column' justify='center' align='center' gap='1rem'>
          <Text.Medium color={theme.color.light.secondary_600} weight='bold'>
            페이지가 존재하지 않거나, 삭제되어 더이상 사용할 수 없는 페이지 입니다.
          </Text.Medium>
          <Text.Medium color={theme.color.light.secondary_600} weight='bold'>
            입력하신 주소가 정확한지 다시 확인하거나 홈으로 이동해주세요.
          </Text.Medium>
        </Flex>
      </Flex>
      <Button
        weight='bold'
        onClick={() => {
          navigate('/');
        }}
      >
        <Text.Medium color={theme.color.light.white}>홈으로 이동</Text.Medium>
      </Button>
    </Flex>
  );
};

export default ErrorPage;
