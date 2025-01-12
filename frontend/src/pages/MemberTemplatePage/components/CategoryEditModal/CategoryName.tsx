import { PropsWithChildren } from 'react';

import { DragIcon } from '@/assets/images';
import { Flex } from '@/components';
import { theme } from '@/style/theme';

const CategoryName = ({ children }: PropsWithChildren) => (
  <Flex align='center' width='100%' height='2.5rem'>
    <DragIcon color={theme.color.light.secondary_400} css={{ marginRight: '0.5rem' }} />
    {children}
  </Flex>
);

export default CategoryName;
