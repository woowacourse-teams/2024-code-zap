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

interface TemplateUploadData {
  templateTitle: string;
  sourceCodeCount: number;
  visibility: string;
}

export const trackClickTemplateSave = ({ templateTitle, sourceCodeCount, visibility }: TemplateUploadData) => {
  amplitudeService.customTrack('[Click] 템플릿 저장 버튼', {
    templateTitle,
    sourceCodeCount,
    visibility,
  });
};

export const trackClickCopyClipBoard = () => {
  amplitudeService.customTrack('[Click] 클립보드 복사 버튼');
};

export const trackClickTemplateShare = () => {
  amplitudeService.customTrack('[Click] 공유 버튼');
};

interface PagingButtonData {
  page: number;
  label: string;
}

export const trackMyTemplatePaging = ({ page, label }: PagingButtonData) => {
  amplitudeService.customTrack('[Click] 페이징 버튼', {
    page,
    label,
  });
};

interface LickButtonData {
  isLiked: boolean;
  likesCount: number;
  templateId: string;
}

export const trackLikeButton = ({ isLiked, likesCount, templateId }: LickButtonData) => {
  const like = isLiked ? '좋아요 취소' : '좋아요';

  amplitudeService.customTrack('[Click] 좋아요 버튼', {
    like,
    likesCount,
    templateId,
  });
};
