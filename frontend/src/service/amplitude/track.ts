import * as amplitude from '@amplitude/analytics-browser';

import { getBrowser } from './getBrowser';

export class AmplitudeService {
  private env: string;
  private browser: string;

  constructor() {
    this.env = process.env.NODE_ENV ?? '';
    this.browser = getBrowser();
  }

  customTrack(eventName: string, eventProps: Record<string, unknown> = {}) {
    amplitude.track(eventName, {
      environment: this.env,
      browser: this.browser,
      ...eventProps,
    });
  }
}

const amplitudeService = new AmplitudeService();

export const trackClickNewTemplate = () => {
  amplitudeService.customTrack('[Click] 새 템플릿 업로드 버튼');
};

export const trackClickTemplateSave = () => {
  amplitudeService.customTrack('[Click] 템플릿 저장 버튼');
};

export const trackClickCopyClipBoard = () => {
  amplitudeService.customTrack('[Click] 클립보드 복사 버튼');
};
