import styled from '@emotion/styled';

import { SettingIcon } from '@/assets/images';
import { Flex } from '@/components';
import { useWindowWidth } from '@/hooks';

const SkeletonButtonWrapper = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;

  width: 12.5rem;
  height: 3rem; /* CategoryButton과 동일한 높이 */
  padding: 0.5rem;

  background: linear-gradient(90deg, #e0e0e0 25%, #c0c0c0 50%, #e0e0e0 75%);
  background-size: 200% 100%;
  border-radius: 8px;
  box-shadow: 1px 2px 8px #00000020; /* CategoryButton과 동일한 그림자 */

  animation: skeleton-loading 1.5s infinite ease-in-out;

  @keyframes skeleton-loading {
    0% {
      background-position: -200% 0;
    }
    100% {
      background-position: 200% 0;
    }
  }
`;

const CategoryListSectionSkeleton = () => {
  const windowWidth = useWindowWidth();

  if (windowWidth <= 768) {
    return null;
  }

  return (
    <Flex direction='column' gap='0.75rem' style={{ marginTop: '4.5rem' }}>
      <Flex justify='flex-end' style={{ opacity: '0.7' }}>
        <SettingIcon width={18} height={18} aria-label='카테고리 편집' />
      </Flex>
      <SkeletonButtonWrapper />
      <SkeletonButtonWrapper />
      <SkeletonButtonWrapper />
    </Flex>
  );
};

export default CategoryListSectionSkeleton;
