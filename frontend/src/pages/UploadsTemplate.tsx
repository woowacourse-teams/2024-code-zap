import { useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/Button';
import { Flex } from '@/components/Flex';
import { SnippetEditor } from '@/components/SnippetEditor';
import { TemplateTitleInput } from '@/components/TemplateTitleInput';
import useTemplateUploadQuery from '@/hooks/useTemplateUploadQuery';

const UploadsTemplate = () => {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [snippets, setSnippets] = useState([
    {
      filename: '',
      content: '',
      ordinal: 1,
    },
  ]);

  const { mutate: uploadTemplate, status, error } = useTemplateUploadQuery();

  const isLoading = status === 'pending';

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

  const handleSaveButtonClick = () => {
    const newTemplate = {
      title,
      representative_snippet_ordinal: 1,
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
      <Flex direction="column" justify="center" align="flex-start" gap="1.5rem" padding="10rem 0">
        <Flex direction="column" justify="center" align="flex-start" gap="1rem" width="100%">
          <TemplateTitleInput
            placeholder="템플릿명을 입력해주세요"
            value={ title }
            onChange={ (e: React.ChangeEvent<HTMLInputElement>) => setTitle(e.target.value) }
          />
          { snippets.map((snippet, idx) => {
            return (
              <SnippetEditor
                key={ idx }
                fileName={ snippet.filename }
                content={ snippet.content }
                onChangeContent={ (newContent) => handleCodeChange(newContent, idx) }
                onChangeFileName={ (newFileName) => handleFileNameChange(newFileName, idx) }
              />
            );
          }) }

          <Flex direction="row" justify="space-between" width="100%" padding="3rem 0 0 0">
            <Button width="auto" type="outlined" onClick={ handleAddButtonClick }>
              + Add Snippet
            </Button>
            <Button onClick={ handleSaveButtonClick } disabled={ isLoading }>
              Save
            </Button>
          </Flex>

          { error && <div style={ { color: 'red' } }>Error: { error.message }</div> }
        </Flex>
      </Flex>
    </>
  );
};

export default UploadsTemplate;
