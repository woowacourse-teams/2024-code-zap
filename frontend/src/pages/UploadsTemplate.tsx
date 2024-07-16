import { useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/Button';
import { Flex } from '@/components/Flex';
import { SnippetEditor } from '@/components/SnippetEditor';
import { TemplateTitleInput } from '@/components/TemplateTitleInput';

const UploadsTemplate = () => {
  const navigate = useNavigate();
  const [snippets, setSnippets] = useState([
    {
      fileName: 'example.js',
      content: 'const a = 1 + 2;',
    },
  ]);

  const handleCodeChange = useCallback((newContent: string, idx: number) => {
    setSnippets((prevSnippets) =>
      prevSnippets.map((snippet, index) => (index === idx ? { ...snippet, content: newContent } : snippet)),
    );
  }, []);

  const handleFileNameChange = useCallback((newFileName: string, idx: number) => {
    setSnippets((prevSnippets) =>
      prevSnippets.map((snippet, index) => (index === idx ? { ...snippet, fileName: newFileName } : snippet)),
    );
  }, []);

  const handleAddButtonClick = () => {
    setSnippets((prevSnippets) => [
      ...prevSnippets,
      {
        fileName: '',
        content: '',
      },
    ]);
  };

  const handleSaveButtonClick = () => {
    navigate('/');
  };

  return (
    <>
      <Flex direction='column' justify='center' align='flex-start' gap='1.5rem' padding='10rem 0'>
        <Flex direction='column' justify='center' align='flex-start' gap='1rem' width='100%'>
          <TemplateTitleInput placeholder='템플릿명을 입력해주세요' />
          {snippets.map((snippet, idx) => {
            return (
              <SnippetEditor
                key={idx}
                fileName={snippet.fileName}
                content={snippet.content}
                onChangeContent={(newContent) => handleCodeChange(newContent, idx)}
                onChangeFileName={(newFileName) => handleFileNameChange(newFileName, idx)}
              />
            );
          })}

          <Flex direction='row' justify='space-between' width='100%' padding='3rem 0 0 0'>
            <Button width='auto' type='outlined' onClick={handleAddButtonClick}>
              + Add Snippet
            </Button>
            <Button onClick={handleSaveButtonClick}>Save</Button>
          </Flex>
        </Flex>
      </Flex>
    </>
  );
};

export default UploadsTemplate;
