import { type LanguageName, loadLanguage } from '@uiw/codemirror-extensions-langs';
import { quietlight } from '@uiw/codemirror-theme-quietlight';
import CodeMirror, { EditorView } from '@uiw/react-codemirror';
import { Link } from 'react-router-dom';

import { CheckCircleIcon, ChevronIcon, ZapzapLogo } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import { SourceCodes } from '@/types';
import { getLanguageByFilename } from '@/utils';

import * as S from './LandingPage.style';

const LandingPage = () => {
  const { isLogin } = useAuth();

  return (
    <S.Container>
      <S.ContentSection>
        <S.TextContent>
          <Text.Medium color='black'>이런 경험 한 번쯤 있으시죠?</Text.Medium>
          <Heading.XSmall color={theme.color.light.secondary_800}>
            {'"아, 그때 그 코드 어디에 썼더라..."'}
          </Heading.XSmall>
          <Flex direction='column' gap='1rem'>
            <Text.Medium color='black'></Text.Medium>
            더이상 코드를 찾느데 헤매지 마세요!
            <Text.Medium color='black'>코드잽에 자주 쓰는 코드를 템플릿으로 저장하고 빠르게 찾아요.</Text.Medium>
          </Flex>
        </S.TextContent>
        <S.ImageWrapper>
          <ZapzapLogo width={250} />
        </S.ImageWrapper>
      </S.ContentSection>

      <S.CardSection>
        <S.Card>
          <Flex align='center' gap='0.25rem'>
            <CheckCircleIcon width={ICON_SIZE.LARGE} />
            <Text.Large color='black'>ZAP하게 저장</Text.Large>
          </Flex>
          <Text.Medium color={theme.color.light.secondary_600}>자주 쓰는 나의 코드를 간편하게 저장하세요</Text.Medium>
        </S.Card>
        <S.Card>
          <Flex align='center' gap='0.25rem'>
            <CheckCircleIcon width={ICON_SIZE.LARGE} />
            <Text.Large color='black'>ZAP하게 관리</Text.Large>
          </Flex>
          <Text.Medium color={theme.color.light.secondary_600}>
            직관적인 분류 시스템으로 체계적으로 관리하세요
          </Text.Medium>
        </S.Card>
        <S.Card>
          <Flex align='center' gap='0.25rem'>
            <CheckCircleIcon width={ICON_SIZE.LARGE} />
            <Text.Large color='black'>ZAP하게 검색</Text.Large>
          </Flex>
          <Text.Medium color={theme.color.light.secondary_600}>필요한 나의 코드를 빠르게 찾아 사용하세요</Text.Medium>
        </S.Card>
      </S.CardSection>

      <S.TemplateSection>
        <ExamCode />
        <Flex direction='column' justify='center' gap='3rem' margin='auto' padding='3rem 0'>
          <Flex direction='column' justify='center' gap='1rem'>
            <Heading.XSmall color='black' weight='bold'>
              템플릿이란?
            </Heading.XSmall>
            <Text.Medium color={theme.color.light.secondary_500}>
              코드잽에서 템플릿이란 반복적으로 작성하게 되는 소스 코드의 모음을 뜻해요.
            </Text.Medium>
            <Text.Medium color={theme.color.light.secondary_500}>
              하나의 템플릿에 여러개의 소스코드를 넣을 수 있어요!
            </Text.Medium>
          </Flex>
          <Flex direction='column' justify='center' gap='1rem'>
            <Heading.XSmall color='black' weight='bold'>
              소스코드란?
            </Heading.XSmall>
            <Text.Medium color={theme.color.light.secondary_500}>
              코드잽에서 소스코드란 파일명 + 소스코드 내용으로 이루어진 코드 단위를 뜻해요.
            </Text.Medium>
            <Text.Medium color={theme.color.light.secondary_500}>
              파일명.[확장자]를 입력하면 하이라이트가 되고 복사 버튼으로 편하게 복사할 수 있어요!
            </Text.Medium>
          </Flex>
        </Flex>
      </S.TemplateSection>

      <S.TemplateSection>
        <S.TextContent>
          <Heading.XSmall color='black'>코드잽은 이런 분들에게 딱이에요 !</Heading.XSmall>
          <Text.Medium color='black'>자주 쓰는 코드 템플릿을 간편하게 저장하고 싶은 분</Text.Medium>
          <Text.Medium color='black'>프로젝트 파일을 뒤적거리는 대신 필요한 코드를 빠르게 찾고 싶은 분</Text.Medium>
          <Text.Medium color='black'>체계적으로 코드를 정리하고 싶지만 방법을 모르셨던 분</Text.Medium>
        </S.TextContent>
        {!isLogin && (
          <Flex direction='column' gap='1rem' width='10rem' margin='2rem 0'>
            <Link to='/login'>
              <Button fullWidth>로그인 하러가기</Button>
            </Link>
            <Link to='/signup'>
              <Button fullWidth>회원가입 하러가기</Button>
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
  const copyCode = (sourceCode: SourceCodes) => () => {
    navigator.clipboard.writeText(sourceCode.content);
    infoAlert('코드가 복사되었습니다!');
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
          <ChevronIcon aria-label='소스코드 펼침' />
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
            loadLanguage(getLanguageByFilename(sourceCode?.filename) as LanguageName) || [],
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
