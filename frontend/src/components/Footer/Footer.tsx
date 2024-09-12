import { Text } from '@/components';

import * as S from './Footer.style';

const Footer = () => (
  <S.FooterContainer>
    <Text.Small color='inherit'>
      Copyright{' '}
      <Text.Small color='inherit' weight='bold'>
        Codezap
      </Text.Small>{' '}
      © All rights reserved.
    </Text.Small>
    <S.ContactEmail href='mailto:codezap2024@gmail.com'>
      <Text.Small color='inherit' weight='bold'>
        문의 :
      </Text.Small>{' '}
      <Text.Small color='inherit'>codezap2024@gmail.com</Text.Small>{' '}
    </S.ContactEmail>
  </S.FooterContainer>
);

export default Footer;
