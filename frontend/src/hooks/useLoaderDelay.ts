import { useEffect, useState } from 'react';

/**
 * useLoaderDelay - 비동기 요청에 대해 로딩 상태를 delay 시키는 훅입니다.
 * @param {boolean} loadingCondition  - 로딩 상태를 판단하는 조건입니다. (ex. isPending, isFetching, ...)
 * @param {number} delay  - 로딩 상태를 delay 시킬 시간입니다.
 */
export const useLoaderDelay = (loadingCondition: boolean, delay: number = 700) => {
  const [showLoader, setShowLoader] = useState(false);

  useEffect(() => {
    let timer: NodeJS.Timeout;

    if (loadingCondition) {
      timer = setTimeout(() => {
        setShowLoader(true);
      }, delay);
    } else {
      setShowLoader(false);
    }

    return () => {
      clearTimeout(timer);
    };
  }, [loadingCondition, delay]);

  return showLoader;
};
