import { ChangeEvent, useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import { Button, Flex, SnippetEditor, TemplateTitleInput } from '@/components';
import { useTemplateQuery } from '@/hooks/template';
import * as S from './TemplateEditPage.style';

const TemplateEditPage = () => {
  const navigate = useNavigate();

  const { id } = useParams<{ id: string }>();
  const { data: template, error, isLoading } = useTemplateQuery(Number(id));

  const [title, setTitle] = useState<string>('');
  const [snippets, setSnippets] = useState<{ filename: string; content: string; ordinal: number }[]>([]);

  useEffect(() => {
    if (template) {
      setTitle(template.title);
      setSnippets([...template.snippets]);
    }
  }, [template]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error.message}</div>;
  }

  if (!template) {
    return <div>No data available</div>;
  }

  const handleAddButtonClick = () => {
    setSnippets((prevSnippets) => [
      ...prevSnippets,
      {
        filename: '',
        content: '',
        ordinal: prevSnippets.length + 1,
      },
    ]);
  };

  const handleCancelButton = () => {
    navigate(`/template/edit/${id}`);
  };

  const handleSaveButtonClick = () => {
    // Send a 'POST' request using custom hook to update the template using a custom hook.
  };

  return (
    <Flex direction='column' align='center' padding='10rem 0 0 0' width='100%'>
      <S.MainContainer>
        <TemplateTitleInput
          placeholder='템플릿명을 입력해주세요'
          value={title}
          onChange={(e: ChangeEvent<HTMLInputElement>) => setTitle(e.target.value)}
        />
        {snippets.map((snippet, idx) => (
          <SnippetEditor
            key={idx}
            fileName={snippet.filename}
            content={snippet.content}
            onChangeContent={() => {}}
            onChangeFileName={() => {}}
          />
        ))}

        <Flex justify='space-between' padding='3rem 0 0 0'>
          <Button size='medium' variant='outlined' onClick={handleAddButtonClick}>
            + Add Snippet
          </Button>
          <Flex gap='1.6rem'>
            <Button size='medium' variant='outlined' onClick={handleCancelButton} disabled={isLoading}>
              cancel
            </Button>
            <Button size='medium' variant='contained' onClick={handleSaveButtonClick} disabled={isLoading}>
              Save
            </Button>
          </Flex>
        </Flex>
      </S.MainContainer>
    </Flex>
  );
};

export default TemplateEditPage;
