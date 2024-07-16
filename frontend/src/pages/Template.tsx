import React, { useRef, useState } from 'react';

import { Flex } from '@/components/Flex';
import { SelectList } from '@/components/SelectList';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { Text } from '@/components/Text';
import mockTemplate from '../mocks/template.json';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

const Template = () => {
  const snippetRefs = useRef<(HTMLDivElement | null)[]>([]);
  const [currentFile, setCurrentFile] = useState<string>(mockTemplate.snippets[0].filename);

  const handleSelectOption = (index: number) => (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();
    snippetRefs.current[index]?.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <>
      <Flex direction='column' justify='center' align='flex-start' gap='1.5rem' padding='10rem 0 0 0' width='100%'>
        <Flex direction='column' justify='center' align='flex-start' gap='1rem'>
          <Text.Title weight='bold'>{mockTemplate.title}</Text.Title>
          <Text.Caption color='#FFD369'>{mockTemplate.member.nickname}</Text.Caption>
        </Flex>

        <Flex direction='row' gap='6rem' width='100%'>
          <Flex direction='column' gap='1rem' flex='0.7'>
            {mockTemplate.snippets.map((snippet, index) => {
              return (
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
                  >
                    {snippet.content}
                  </SyntaxHighlighter>
                </div>
              );
            })}
          </Flex>

          <SelectList>
          {mockTemplate.snippets.map((snippet, index) => (
            <SelectList.Option
              key={snippet.id}
              onClick={handleSelectOption(index, snippet.filename)}
              isSelected={currentFile === snippet.filename}
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
