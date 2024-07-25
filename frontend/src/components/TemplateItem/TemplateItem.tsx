import { Flex } from '../Flex';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { Text } from '../Text';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { TemplateListItem } from '@/types/template';

interface Props {
  item: TemplateListItem;
}

const TemplateItem = ({ item }: Props) => {
  const { title, member, modified_at, representative_snippet } = item;
  const [year, month, day] = modified_at.split(' ')[0].split('-');
  return (
    <Flex direction='column' gap='1.2rem' width='100%'>
      <Flex direction='column' justify='flex-start' align='flex-start' width='100%' gap='0.8rem'>
        <Text.SubTitle weight='bold'>{title}</Text.SubTitle>
        <Text.Caption>{member.nickname}</Text.Caption>
      </Flex>

      <SyntaxHighlighter
        language='javascript'
        style={vscDarkPlus}
        showLineNumbers={true}
        customStyle={{ borderRadius: '10px', width: '100%', tabSize: 2 }}
      >
        {representative_snippet.content_summary}
      </SyntaxHighlighter>

      <Text.Caption>
        {year}년 {month}월 {day}일
      </Text.Caption>
    </Flex>
  );
};

export default TemplateItem;
