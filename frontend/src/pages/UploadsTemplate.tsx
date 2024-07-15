import { Button } from '@/components/Button';
import { Flex } from '@/components/Flex';
import ReactCodeMirror from '@uiw/react-codemirror';
import { javascript } from '@codemirror/lang-javascript';
import { useCallback, useState } from 'react';
import { vscodeDark } from '@uiw/codemirror-theme-vscode';
import { useNavigate } from 'react-router-dom';

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
      <Flex direction='column' justify='center' align='flex-start' gap='1.5rem' padding='10rem 0 0 0'>
        <Flex direction='column' justify='center' align='flex-start' gap='1rem' width='100%'>
          <input placeholder='템플릿명을 입력해주세요' style={{ width: '100%' }}></input>

          {snippets.map((snippet, idx) => {
            return (
              <ReactCodeMirror
                key={idx}
                value={snippet.content}
                height='200px'
                style={{ width: '100%', borderRadius: '20px' }}
                theme={vscodeDark}
                extensions={[javascript({ jsx: true })]}
                onChange={(val) => handleCodeChange(val, idx)}
              />
            );
          })}
          <Flex direction='row' justify='space-between' width='100%'>
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
