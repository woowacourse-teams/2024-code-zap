import { Text } from '@/components';
import { useAuth } from '@/hooks/authentication';

import * as S from './Footer.style';

const Footer = () => {
  const { isChecking } = useAuth();

  if (isChecking) {
    return null;
  }

  return (
    <S.FooterContainer>
      <Text.Small color='inherit'>
        Copyright{' '}
        <Text.Small color='inherit' weight='bold'>
          Codezap
        </Text.Small>{' '}
        © All rights reserved.
      </Text.Small>
      <S.ContactEmail>
        <Text.Small color='inherit'>문의: codezap2024@gmail.com</Text.Small>
      </S.ContactEmail>
    </S.FooterContainer>
  );
};

export default Footer;
