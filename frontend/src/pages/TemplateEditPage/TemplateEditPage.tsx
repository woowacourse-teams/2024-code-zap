import { ChangeEvent, useCallback, useState } from 'react';

import { trashcanIcon } from '@/assets/images';
import { Button, Flex, SnippetEditor, TemplateTitleInput } from '@/components';
import { useTemplateEditQuery } from '@/hooks/template';
import { Template } from '@/types/template';
import * as S from './TemplateEditPage.style';

interface Props {
  template: Template;
  toggleEditButton: () => void;
}

const TemplateEditPage = ({ template, toggleEditButton }: Props) => {
  const [title, setTitle] = useState<string>(template.title);
  const [snippets, setSnippets] = useState([...template.snippets]);
  const [deleteSnippetIds, setDeleteSnippetIds] = useState<number[]>([]);

  const { mutateAsync } = useTemplateEditQuery(template.id);

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
    toggleEditButton();
  };

  const handleCodeChange = useCallback((newContent: string, idx: number) => {
    setSnippets((prevSnippets) =>
      prevSnippets.map((snippet, index) => (index === idx ? { ...snippet, content: newContent } : snippet)),
    );
  }, []);

  const handleFileNameChange = useCallback((newFileName: string, idx: number) => {
    setSnippets((prevSnippets) =>
      prevSnippets.map((snippet, index) => (index === idx ? { ...snippet, filename: newFileName } : snippet)),
    );
  }, []);

  const handleDeleteSnippet = (index: number) => {
    const deletedSnippetId = snippets[index].id;

    if (!snippets[index]) {
      console.error('존재하지 않는 스니펫 인덱스입니다.');
    }

    if (deletedSnippetId) {
      setDeleteSnippetIds((prevSnippetsId) => [...prevSnippetsId, deletedSnippetId]);
    }

    setSnippets((prevSnippets) => prevSnippets.filter((_, idx) => index !== idx));
  };

  const handleSaveButtonClick = async () => {
    const orderedSnippets = snippets.map((snippet, index) => ({
      ...snippet,
      ordinal: index + 1,
    }));
    const createSnippets = orderedSnippets.filter((snippet) => !snippet.id);
    const updateSnippets = orderedSnippets.filter((snippet) => snippet.id);

    const templateUpdate = {
      title,
      createSnippets,
      updateSnippets,
      deleteSnippetIds,
    };

    try {
      await mutateAsync({ id: template.id, template: templateUpdate });
      toggleEditButton();
    } catch (error) {
      console.error('Failed to update template:', error);
    }
  };

  return (
    <Flex direction='column' align='center' width='100%'>
      <S.MainContainer>
        <TemplateTitleInput
          placeholder='템플릿명을 입력해주세요'
          value={title}
          onChange={(e: ChangeEvent<HTMLInputElement>) => setTitle(e.target.value)}
        />
        {snippets.map((snippet, idx) => (
          <Flex key={idx} style={{ position: 'relative' }}>
            <SnippetEditor
              key={idx}
              fileName={snippet.filename}
              content={snippet.content}
              onChangeContent={(newContent) => handleCodeChange(newContent, idx)}
              onChangeFileName={(newFileName) => handleFileNameChange(newFileName, idx)}
            />
            <S.DeleteButton
              size='small'
              variant='text'
              onClick={() => {
                handleDeleteSnippet(idx);
              }}
            >
              <img src={trashcanIcon} width={20} height={20} alt='Delete snippet' />
            </S.DeleteButton>
          </Flex>
        ))}

        <Flex justify='space-between' padding='3rem 0 0 0'>
          <Button size='medium' variant='outlined' onClick={handleAddButtonClick}>
            + Add Snippet
          </Button>
          <Flex gap='1.6rem'>
            <Button size='medium' variant='outlined' onClick={handleCancelButton}>
              cancel
            </Button>
            <Button size='medium' variant='contained' onClick={handleSaveButtonClick} disabled={snippets.length === 0}>
              Save
            </Button>
          </Flex>
        </Flex>
      </S.MainContainer>
    </Flex>
  );
};

export default TemplateEditPage;
