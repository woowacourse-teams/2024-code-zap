import { theme } from '@design/style/theme';
import {
  type LanguageName,
  loadLanguage,
} from '@uiw/codemirror-extensions-langs';
import { quietlight } from '@uiw/codemirror-theme-quietlight';
import CodeMirror, { EditorView } from '@uiw/react-codemirror';
import { useTranslation } from 'react-i18next';
import { Link } from 'react-router-dom';

import { CheckCircleIcon, ChevronIcon, ZapzapLogo } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { useTrackPageViewed } from '@/service/amplitude';
import { ICON_SIZE } from '@/style/styleConstants';
import { SourceCodes } from '@/types';
import { getLanguageByFilename } from '@/utils';

import * as S from './LandingPage.style';

const LandingPage = () => {
  useTrackPageViewed({ eventName: '[Viewed] 랜딩 페이지' });

  const { isLogin } = useAuth();

  const { t } = useTranslation('LandingPage');

  const EXPLAIN = [
    {
      title: t('ex_title_1'),
      description: t('ex_desc_1'),
    },
    {
      title: t('ex_title_2'),
      description: t('ex_desc_2'),
    },
    {
      title: t('ex_title_3'),
      description: t('ex_desc_3'),
    },
  ];

  return (
    <S.Container>
      <S.ContentSection>
        <S.TextContent>
          <Text.Medium color='black'>{t('question')}</Text.Medium>
          <Heading.XSmall color={theme.color.light.secondary_800}>
            {t('quote')}
          </Heading.XSmall>
          <Flex direction='column' gap='1rem'>
            <Text.Medium color='black'>{t('no_more_searching')}</Text.Medium>
            <Text.Medium color='black'>{t('intro_text')}</Text.Medium>
          </Flex>
        </S.TextContent>
        <S.ImageWrapper>
          <ZapzapLogo width={250} />
        </S.ImageWrapper>
      </S.ContentSection>

      <S.CardSection>
        {EXPLAIN.map((el, idx) => (
          <S.Card key={idx}>
            <Flex align='center' gap='0.25rem'>
              <CheckCircleIcon width={ICON_SIZE.LARGE} />
              <Text.Large color='black'>{el.title}</Text.Large>
            </Flex>
            <Text.Medium color={theme.color.light.secondary_600}>
              {el.description}
            </Text.Medium>
          </S.Card>
        ))}
      </S.CardSection>

      <S.TemplateSection>
        <ExamCode />
        <Flex
          direction='column'
          justify='center'
          gap='3rem'
          margin='auto'
          padding='3rem 0'
        >
          <Flex direction='column' justify='center' gap='1rem'>
            <Heading.XSmall color='black' weight='bold'>
              {t('what_is_template')}
            </Heading.XSmall>
            <Text.Medium color={theme.color.light.secondary_500}>
              {t('template_explanation_1')}
            </Text.Medium>
            <Text.Medium color={theme.color.light.secondary_500}>
              {t('template_explanation_2')}
            </Text.Medium>
          </Flex>
          <Flex direction='column' justify='center' gap='1rem'>
            <Heading.XSmall color='black' weight='bold'>
              {t('what_is_source_code')}
            </Heading.XSmall>
            <Text.Medium color={theme.color.light.secondary_500}>
              {t('source_code_explanation_1')}
            </Text.Medium>
            <Text.Medium color={theme.color.light.secondary_500}>
              {t('source_code_explanation_2')}
            </Text.Medium>
          </Flex>
        </Flex>
      </S.TemplateSection>

      <S.TemplateSection>
        <S.TextContent>
          <Heading.XSmall color='black'>{t('target_title')}</Heading.XSmall>
          <Text.Medium color='black'>{t('target_2')}</Text.Medium>
          <Text.Medium color='black'>{t('target_3')}</Text.Medium>
          <Text.Medium color='black'>{t('target_3')}</Text.Medium>
        </S.TextContent>
        {!isLogin && (
          <Flex direction='column' gap='1rem' width='10rem' margin='2rem 0'>
            <Link to='/login'>
              <Button fullWidth>{t('go_login')}</Button>
            </Link>
            <Link to='/signup'>
              <Button fullWidth>{t('go_signup')}</Button>
            </Link>
          </Flex>
        )}
      </S.TemplateSection>
    </S.Container>
  );
};

export default LandingPage;

const ExamCode = () => {
  const sourceCode = {
    id: 102,
    filename: 'App.tsx',
    content:
      "import React from 'react';\nimport MyComponent from './MyComponent';\n\nconst Template = () => {\n  return (\n    <div>\n      <MyComponent name=\"code zap\" />\n    </div>\n  );\n};\n\nexport default Template;",
    ordinal: 2,
  };

  const { infoAlert } = useCustomContext(ToastContext);
  const { t } = useTranslation('LandingPage');
  const copyCode = (sourceCode: SourceCodes) => () => {
    navigator.clipboard.writeText(sourceCode.content);
    infoAlert(t('copy'));
  };

  return (
    <S.CodeSection>
      <Flex
        justify='space-between'
        align='center'
        height='3rem'
        padding='1rem 1.5rem'
        style={{ background: '#393e46', borderRadius: '8px 8px 0 0' }}
      >
        <Flex align='center' gap='0.5rem' css={{ cursor: 'pointer' }}>
          <ChevronIcon aria-label={t('expand_source_code')} />
          <Text.Small color='#fff' weight='bold'>
            {sourceCode.filename}
          </Text.Small>
        </Flex>
        <Button size='small' variant='text' onClick={copyCode(sourceCode)}>
          <Text.Small color={theme.color.light.primary_500} weight='bold'>
            {'복사'}
          </Text.Small>
        </Button>
      </Flex>
      <S.SyntaxHighlighterWrapper isOpen>
        <CodeMirror
          value={sourceCode.content}
          height='100%'
          style={{ width: '100%', fontSize: '1rem' }}
          theme={quietlight}
          extensions={[
            loadLanguage(
              getLanguageByFilename(sourceCode?.filename) as LanguageName,
            ) || [],
            S.CustomCodeMirrorTheme,
            EditorView.editable.of(false),
          ]}
          css={{
            '.cm-editor': {
              borderRadius: '0 0 8px 8px',
              overflow: 'hidden',
            },
            '.cm-scroller': {
              padding: '1rem 0',
              overflowY: 'auto',
              height: '100%',
            },
          }}
        />
      </S.SyntaxHighlighterWrapper>
    </S.CodeSection>
  );
};
