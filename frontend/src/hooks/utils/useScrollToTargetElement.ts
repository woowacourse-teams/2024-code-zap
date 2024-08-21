import { useHeaderHeight } from './useHeaderHeight';

export const useScrollToTargetElement = () => {
  const { headerHeight } = useHeaderHeight();

  const scrollToTargetElement = (targetElement: HTMLDivElement | null) => {
    if (targetElement) {
      const elementPosition = targetElement.getBoundingClientRect().top;
      const offsetPosition = elementPosition + window.scrollY - (headerHeight + 10);

      window.scrollTo({
        top: offsetPosition,
        behavior: 'smooth',
      });
    }
  };

  return scrollToTargetElement;
};
