import styled from '@emotion/styled';
import { type LanguageName, loadLanguage } from '@uiw/codemirror-extensions-langs';
import { quietlight } from '@uiw/codemirror-theme-quietlight';
import CodeMirror, { EditorView } from '@uiw/react-codemirror';

import { ChevronIcon, CodeZapLogo } from '@/assets/images';
import { Button, Flex, Heading, Text } from '@/components';
import { CustomCodeMirrorTheme } from '@/components/TemplateCard/TemplateCard.style';
import { theme } from '@/style/theme';
import { getLanguageByFilename } from '@/utils';
import * as S from '../TemplatePage/TemplatePage.style';

const LandingPage = () => (
  <Flex direction='column' justify='center' align='center' height='100vh' gap='2rem' width='70rem'>
    <Flex justify='center' align='center' gap='1rem' width='27.5rem'>
      <CodeZapLogo aria-label='로고 버튼' width={100} height={50} />
      <Heading.XLarge color='#F79037'>코드잽</Heading.XLarge>
    </Flex>

    <Flex direction='column' justify='center' align='center' gap='2rem'>
      <Heading.XSmall color='black'>자주 쓰는 코드를 나만의 템플릿으로 저장해보세요.</Heading.XSmall>
      <Heading.XSmall color='black'> 코드잽에서 나의 코드를 빠르게 찾고, 효율적으로 관리할 수 있습니다.</Heading.XSmall>
    </Flex>

    <Flex justify='center' align='center' gap='1rem'>
      <Card>
        <Heading.XSmall color='white'>💾 간편한 저장</Heading.XSmall>
        <Text.Medium color='white' weight='bold'>
          자주 쓰는 나의 코드를 ZAP하게 저장하세요.
        </Text.Medium>
      </Card>

      <Card>
        <Heading.XSmall color='white'>🔍 빠른 검색</Heading.XSmall>
        <Text.Medium color='white' weight='bold'>
          필요한 나의 코드를 ZAP하게 찾아 사용하세요.
        </Text.Medium>
      </Card>

      <Card>
        <Heading.XSmall color='white'>📊 체계적인 관리</Heading.XSmall>
        <Text.Medium color='white' weight='bold'>
          직관적인 분류 시스템으로 ZAP하게 정리하세요.
        </Text.Medium>
      </Card>
    </Flex>

    <Flex justify='center' align='center' gap='3rem'>
      <ExamCode />
      <Flex direction='column' gap='5rem'>
        <Flex direction='column' gap='1.5rem'>
          <Heading.XSmall color='black' weight='bold'>
            ⚡️ 템플릿이란?
          </Heading.XSmall>
          <Text.XLarge color='black' weight='bold'>
            템플릿은 반복적으로 작성하게 되는 코드 블럭 모음
          </Text.XLarge>
        </Flex>
        <Flex direction='column' gap='1.5rem'>
          <Heading.XSmall color='black' weight='bold'>
            🙌 코드잽은 이런 분들에게 딱이에요
          </Heading.XSmall>
          <Text.XLarge color='black' weight='bold'>
            • 자주 쓰는 코드 템플릿을 간편하게 저장하고 싶은 분
          </Text.XLarge>
          <Text.XLarge color='black' weight='bold'>
            • 프로젝트 파일을 뒤적거리는 대신 필요한 코드를 빠르게 찾고 싶은 분
          </Text.XLarge>
          <Text.XLarge color='black' weight='bold'>
            • 체계적으로 코드를 정리하고 싶지만 방법을 모르셨던 분
          </Text.XLarge>
        </Flex>
      </Flex>
    </Flex>
  </Flex>
);

export default LandingPage;

const ExamCode = () => {
  const sourceCode = {
    id: 102,
    filename: 'App.tsx',
    content:
      "import React from 'react';\nimport MyComponent from './MyComponent';\n\nconst App: React.FC = () => {\n  return (\n    <div>\n      <MyComponent name=\"John Doe\" age={30} />\n    </div>\n  );\n};\n\nexport default App;",
    ordinal: 2,
  };

  return (
    <div>
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
        <Button size='small' variant='text'>
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
            CustomCodeMirrorTheme,
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
    </div>
  );
};

const Card = styled.div`
  cursor: pointer;

  display: flex;
  flex-direction: column;
  gap: 2rem;

  padding: 2rem;

  background-color: ${theme.color.light.primary_500};
  border-radius: 24px;

  transition: 0.1s ease;
  &:hover {
    bottom: 0.5rem;
    transform: scale(1.025);
    box-shadow: 1px 2px 8px 1px #00000030;
  }
`;
