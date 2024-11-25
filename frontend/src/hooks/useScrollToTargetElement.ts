import { useHeaderHeight } from '@/hooks';

export const useScrollToTargetElement = () => {
  const { headerHeight } = useHeaderHeight();

  const scrollToTargetElement = (targetElement: HTMLDivElement | null) => {
    if (targetElement) {
      const elementPosition = targetElement.getBoundingClientRect().top;
      const offsetPosition = elementPosition + window.scrollY - headerHeight;

      window.scrollTo({
        top: offsetPosition,
        behavior: 'smooth',
      });
    }
  };

  return scrollToTargetElement;
};
