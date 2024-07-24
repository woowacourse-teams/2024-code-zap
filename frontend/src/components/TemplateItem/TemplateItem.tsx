import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { TemplateListItem } from '@/types/template';
import { formatRelativeTime } from '@/utils/formatRelativeTime';
import { Flex } from '../Flex';
import { Text } from '../Text';

interface Props {
  item: TemplateListItem;
}

const TemplateItem = ({ item }: Props) => {
  const { title, modifiedAt, thumbnailSnippet } = item;

  return (
    <Flex direction='column' gap='1.2rem' width='100%'>
      <Flex direction='column' justify='flex-start' align='flex-start' width='100%' gap='0.8rem'>
        <Text.SubTitle color='white'>{title}</Text.SubTitle>
      </Flex>

      <SyntaxHighlighter
        language='javascript'
        style={vscDarkPlus}
        showLineNumbers={true}
        customStyle={{ borderRadius: '10px', width: '100%', tabSize: 2 }}
        codeTagProps={{
          style: {
            fontSize: '1.8rem',
          },
        }}
      >
        {thumbnailSnippet.thumbnailContent}
      </SyntaxHighlighter>

      <Text.Caption color='white'>{formatRelativeTime(modifiedAt)}</Text.Caption>
    </Flex>
  );
};

export default TemplateItem;
