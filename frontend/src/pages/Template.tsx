import { Flex } from '@/components/Flex';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { Text } from '@/components/Text';
import mockTemplate from '../mocks/template.json';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

const Template = () => {
  return (
    <>
      <Flex
        direction='column'
        justify='center'
        align='flex-start'
        gap='1.5rem'
        padding='10rem 0 0 0'
      >
        <Flex direction='column' justify='center' align='flex-start' gap='1rem'>
          <Text.Title weight='bold'>{mockTemplate.title}</Text.Title>
          <Text.Caption color='#FFD369'>
            {mockTemplate.member.nickname}
          </Text.Caption>
        </Flex>

        {mockTemplate.snippets.map((snippet) => {
          return (
            <SyntaxHighlighter
              language='javascript'
              style={vscDarkPlus}
              showLineNumbers={true}
              customStyle={{
                borderRadius: '10px',
                width: '70%',
                tabSize: 2,
              }}
            >
              {snippet.content}
            </SyntaxHighlighter>
          );
        })}
      </Flex>
    </>
  );
};

export default Template;
