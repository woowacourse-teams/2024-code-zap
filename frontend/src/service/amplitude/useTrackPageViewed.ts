import { useEffect } from 'react';

import { AmplitudeService } from './track';

interface Props {
  eventName: string;
  eventProps?: Record<string, unknown>;
}

/**
 * browser, env를 제외한 eventProps를 제공해주세요
 */
export const useTrackPageViewed = ({ eventName, eventProps }: Props) => {
  const amplitudeService = new AmplitudeService();

  useEffect(() => {
    amplitudeService.customTrack(eventName, eventProps ?? {});
  }, []);
};
