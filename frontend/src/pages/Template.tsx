import { useRef, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { Flex } from '@/components/Flex';
import { SelectList } from '@/components/SelectList';
import { Text } from '@/components/Text';
import { useTemplateQuery } from '@/hooks/useTemplateQuery';
import { formatRelativeTime } from '@/utils/formatRelativeTime';

const Template = () => {
  const { id } = useParams<{ id: string }>();
  const { data: template, error, isLoading } = useTemplateQuery(Number(id));
  const snippetRefs = useRef<(HTMLDivElement | null)[]>([]);
  const [currentFile, setCurrentFile] = useState(template?.snippets[0].id);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error.message}</div>;
  }

  if (!template) {
    return <div>No data available</div>;
  }

  const handleSelectOption = (index: number) => (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();
    snippetRefs.current[index]?.scrollIntoView({ behavior: 'smooth' });
    setCurrentFile(template.snippets[index].id);
  };

  return (
    <>
      <Flex direction='column' justify='center' align='flex-start' gap='1.5rem' padding='10rem 0 0 0' width='100%'>
        <Flex direction='column' justify='center' align='flex-start' gap='2rem'>
          <Text.Title color='white' weight='bold'>
            {template.title}
          </Text.Title>
          <Text.Caption weight='bold' color='#ffd369'>
            {formatRelativeTime(template.modifiedAt)}
          </Text.Caption>
        </Flex>

        <Flex direction='row' gap='6rem' width='100%'>
          <Flex direction='column' gap='1rem' flex='0.7'>
            {template.snippets.map((snippet, index) => (
              <div id={snippet.filename} key={snippet.id} ref={(el) => (snippetRefs.current[index] = el)}>
                <SyntaxHighlighter
                  language='javascript'
                  style={vscDarkPlus}
                  showLineNumbers={true}
                  customStyle={{
                    borderRadius: '10px',
                    width: '100%',
                    tabSize: 2,
                  }}
                  codeTagProps={{
                    style: {
                      fontSize: '1.8rem',
                    },
                  }}
                >
                  {snippet.content}
                </SyntaxHighlighter>
              </div>
            ))}
          </Flex>

          <SelectList>
            {template.snippets.map((snippet, index) => (
              <SelectList.Option
                key={snippet.id}
                onClick={handleSelectOption(index)}
                isSelected={currentFile === snippet.id}
              >
                {snippet.filename}
              </SelectList.Option>
            ))}
          </SelectList>
        </Flex>
      </Flex>
    </>
  );
};

export default Template;
