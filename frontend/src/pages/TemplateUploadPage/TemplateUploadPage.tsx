import { useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { trashcanIcon } from '@/assets/images';
import { Button, Flex, SnippetEditor, TemplateTitleInput } from '@/components';
import { useTemplateUploadQuery } from '@/hooks/template';
import type { TemplateUploadRequest } from '@/types';
import * as S from './TemplateUploadPage.style';

const TemplateUploadPage = () => {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [snippets, setSnippets] = useState([
    {
      filename: '',
      content: '',
      ordinal: 1,
    },
  ]);

  const { mutate: uploadTemplate, error } = useTemplateUploadQuery();

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

  const handleDeleteSnippet = (index: number) => {
    if (!snippets[index]) {
      console.error('존재하지 않는 스니펫 인덱스입니다.');
    }

    setSnippets((prevSnippets) => prevSnippets.filter((_, idx) => index !== idx));
  };

  const handleCancelButton = () => {
    navigate(-1);
  };

  const handleSaveButtonClick = () => {
    const newTemplate: TemplateUploadRequest = {
      title,
      snippets,
    };

    uploadTemplate(newTemplate, {
      onSuccess: () => {
        navigate('/');
      },
    });
  };

  return (
    <>
      <Flex direction='column' justify='center' align='flex-start' gap='1.5rem'>
        <Flex direction='column' justify='center' align='flex-start' gap='1rem' width='100%'>
          <TemplateTitleInput
            placeholder='템플릿명을 입력해주세요'
            value={title}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) => setTitle(e.target.value)}
          />
          {snippets.map((snippet, idx) => (
            <Flex key={idx} style={{ position: 'relative' }} width='100%'>
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

          <Flex justify='space-between' padding='3rem 0 0 0' width='100%'>
            <Button size='medium' variant='outlined' onClick={handleAddButtonClick}>
              + Add Snippet
            </Button>
            <Flex gap='1.6rem'>
              <Button size='medium' variant='outlined' onClick={handleCancelButton}>
                cancel
              </Button>
              <Button
                size='medium'
                variant='contained'
                onClick={handleSaveButtonClick}
                disabled={snippets.length === 0}
              >
                Save
              </Button>
            </Flex>
          </Flex>

          {error && <div style={{ color: 'red' }}>Error: {error.message}</div>}
        </Flex>
      </Flex>
    </>
  );
};

export default TemplateUploadPage;
