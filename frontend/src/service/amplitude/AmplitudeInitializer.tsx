import * as amplitude from '@amplitude/analytics-browser';
import { useEffect, useRef } from 'react';

import { useAuth } from '@/hooks/authentication';

const amplitudeApiKey = process.env.AMPLITUDE_API_KEY ?? '';

const AmplitudeInitializer = ({ children }: React.PropsWithChildren) => {
  const {
    memberInfo: { name },
  } = useAuth();

  const isAmplitudeInitialized = useRef(false);
  const prevName = useRef<string | undefined>(undefined);

  useEffect(() => {
    // localhost, 개발 서버 환경일 때는 init X
    if (process.env.APP_ENV === 'dev') {
      return;
    }

    // Amplitude가 처음 초기화될 때 (여러 번 init 되는 것을 방지)
    if (isAmplitudeInitialized.current === false) {
      if (name) {
        amplitude.init(amplitudeApiKey, name, {
          defaultTracking: {
            pageViews: false,
          },
          minIdLength: 1,
        });
      } else {
        amplitude.init(amplitudeApiKey, undefined, {
          defaultTracking: {
            pageViews: false,
          },
          minIdLength: 1,
        });
      }

      isAmplitudeInitialized.current = true;

      // name이 이전 값과 다를 때 (로그인 유저가 달라짐)
    } else if (prevName.current !== name && name !== undefined) {
      amplitude.init(amplitudeApiKey, name, {
        defaultTracking: {
          pageViews: false,
        },
        minIdLength: 1,
      });
    }

    // 이전 name 값을 업데이트
    prevName.current = name;
  }, [name]);

  return <>{children}</>;
};

export default AmplitudeInitializer;
