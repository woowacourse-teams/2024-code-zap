import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

import { Flex, Text } from '@/components';
import { TemplateListItem } from '@/types/template';
import { formatRelativeTime } from '@/utils';

interface Props {
  item: TemplateListItem;
}

const TemplateItem = ({ item }: Props) => {
  const { title, modifiedAt, thumbnailSnippet } = item;

  return (
    <Flex direction='column' gap='1.6rem' width='100%'>
      <Flex direction='column' justify='flex-start' align='flex-start' width='100%' gap='0.8rem'>
        <Text.SubTitle color='white'>{title}</Text.SubTitle>
      </Flex>
      <Flex direction='column'>
        <Flex
          align='center'
          height='3rem'
          padding='1rem 1.5rem'
          style={{ background: '#393e46', borderRadius: '8px 8px 0 0' }}
        >
          <Text.Caption color='#fff' weight='bold'>
            {thumbnailSnippet.filename}
          </Text.Caption>
        </Flex>
        <SyntaxHighlighter
          language='javascript'
          style={vscDarkPlus}
          showLineNumbers={true}
          customStyle={{ margin: 0, borderRadius: '0 0 8px 8px', width: '100%', tabSize: 2 }}
          codeTagProps={{
            style: {
              fontSize: '1.8rem',
              lineHeight: '1.2rem',
            },
          }}
        >
          {thumbnailSnippet.thumbnailContent}
        </SyntaxHighlighter>
      </Flex>

      <Text.Caption color='white'>{formatRelativeTime(modifiedAt)}</Text.Caption>
    </Flex>
  );
};

export default TemplateItem;
