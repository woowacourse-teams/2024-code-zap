import { useCallback, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { Button } from '@/components/Button';
import { Flex } from '@/components/Flex';
import { SnippetEditor } from '@/components/SnippetEditor';

const UploadsTemplate = () => {
  const navigate = useNavigate();
  const [snippets, setSnippets] = useState([
    {
      title: 'example.js',
      content: 'const a = 1 + 2;',
    },
  ]);

  const handleCodeChange = useCallback((val: string, idx: number) => {
    setSnippets((prevSnippets) =>
      prevSnippets.map((snippet, index) => (index === idx ? { ...snippet, content: val } : snippet)),
    );
  }, []);

  const handleAddButtonClick = () => {
    setSnippets((prevSnippets) => [
      ...prevSnippets,
      {
        title: '',
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
          <Input placeholder='템플릿명을 입력해주세요'></Input>
          {snippets.map((snippet, idx) => {
            return <SnippetEditor key={idx} content={snippet.content} onChange={(val) => handleCodeChange(val, idx)} />;
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

const inputStyles = css`
  width: 100%;
  padding: 10px 0;
  background: none;
  border: none;
  border-bottom: 1px solid #555555;
  color: #cccccc;
  font-size: 16px;

  &::placeholder {
    color: #808080;
  }

  &:focus {
    outline: none;
    border-bottom: 1px solid #cccccc;
  }
`;

const InputWrapper = styled.div`
  position: relative;
  margin: 20px 0;
  width: 100%;
`;

const StyledInput = styled.input`
  ${inputStyles}
`;

const Input = ({ placeholder }: { placeholder: string }) => {
  return (
    <InputWrapper>
      <StyledInput placeholder={placeholder} />
    </InputWrapper>
  );
};
