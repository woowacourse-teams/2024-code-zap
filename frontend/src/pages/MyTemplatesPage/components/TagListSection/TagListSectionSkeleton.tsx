import styled from '@emotion/styled';

const skeletonAnimation = `
  @keyframes skeleton-loading {
    0% {
      background-position: -200% 0;
    }
    100% {
      background-position: 200% 0;
    }
  }
`;

const TagListSectionSkeleton = styled.div`
  ${skeletonAnimation}
  box-sizing: content-box;
  width: 100%;
  height: 3.875rem;

  background: linear-gradient(90deg, #e0e0e0 25%, #c0c0c0 50%, #e0e0e0 75%);
  background-size: 200% 100%;
  border: 1px solid #c0c0c0;
  border-radius: 8px;

  animation: skeleton-loading 2.5s infinite ease-in-out;
`;

export default TagListSectionSkeleton;
