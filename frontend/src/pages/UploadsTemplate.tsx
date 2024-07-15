import { Button } from '@/components/Button';
import { Flex } from '@/components/Flex';
import { useState } from 'react';

const UploadsTemplate = () => {
  const [snippets, setSnippets] = useState([
    {
      title: 'example.js',
      content: 'const a = 1 + 2;',
    },
    {
      title: 'module.js',
      content: 'const a = 1 + 2;',
    },
  ]);

  return (
    <>
      <Flex direction='column' justify='center' align='flex-start' gap='1.5rem' padding='10rem 0 0 0'>
        <Flex direction='column' justify='center' align='flex-start' gap='1rem' width='100%'>
          <input placeholder='템플릿명을 입력해주세요' style={{ width: '100%' }}></input>

          {snippets.map((snippet, idx) => {
            return <textarea key={idx} style={{ width: '100%', height: '10rem' }}></textarea>;
          })}
          <Flex direction='row' justify='space-between' width='100%'>
            <Button width='auto' type='outlined'>
              + Add Snippet
            </Button>
            <Button>Save</Button>
          </Flex>
        </Flex>
      </Flex>
    </>
  );
};

export default UploadsTemplate;
