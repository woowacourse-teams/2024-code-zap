import { useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

import { Button, Flex, SelectList, Text } from '@/components';
import { useTemplateQuery } from '@/hooks/template';
import { formatRelativeTime } from '@/utils';
import { MainContainer, SidebarContainer } from './TemplatePage.style';

const TemplatePage = () => {
  const navigate = useNavigate();
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

  const handleEditButtonClick = () => {
    navigate(`/templates/edit/${id}`);
  };

  const handleSelectOption = (index: number) => (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();
    snippetRefs.current[index]?.scrollIntoView({ behavior: 'smooth' });
    setCurrentFile(template.snippets[index].id);
  };

  return (
    <Flex direction='column' align='center' padding='10rem 0 0 0' width='100%'>
      <MainContainer>
        <Flex justify='space-between'>
          <Flex direction='column' gap='1.6rem'>
            <Text.Title color='white'>{template.title}</Text.Title>
            <Text.Caption color='#ffd369' weight='bold'>
              {formatRelativeTime(template.modifiedAt)}
            </Text.Caption>
          </Flex>
          <Flex align='flex-end' gap='1.6rem'>
            <Button variant='outlined' size='medium' onClick={handleEditButtonClick}>
              Edit
            </Button>
            <Button variant='outlined' size='medium' style={{ backgroundColor: '#BF3015' }}>
              Delete
            </Button>
          </Flex>
        </Flex>

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
      </MainContainer>

      <SidebarContainer>
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
      </SidebarContainer>
    </Flex>
  );
};

export default TemplatePage;
