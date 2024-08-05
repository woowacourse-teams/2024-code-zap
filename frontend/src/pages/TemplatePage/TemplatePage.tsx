import { useTheme } from '@emotion/react';
import { useParams } from 'react-router-dom';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

import { pencilIcon, trashcanIcon } from '@/assets/images';
import { Flex, Heading, SelectList, Text } from '@/components';
import { useTemplate } from '@/hooks/template/useTemplate';
import { TemplateEditPage } from '@/pages';
import { formatRelativeTime, getLanguageByFilename } from '@/utils';
import * as S from './TemplatePage.style';

const TemplatePage = () => {
  const { id } = useParams<{ id: string }>();
  const theme = useTheme();

  const {
    currentFile,
    template,
    isEdit,
    snippetRefs,
    toggleEditButton,
    handleEditButtonClick,
    handleSelectOption,
    handleDelete,
  } = useTemplate(Number(id));

  if (!template) {
    return <div>템플릿을 불러오는 중...</div>;
  }

  return (
    <>
      {isEdit ? (
        <TemplateEditPage template={template} toggleEditButton={toggleEditButton} />
      ) : (
        <Flex
          direction='column'
          align='center'
          width='100%'
          css={{ maxWidth: '53rem', margin: 'auto', marginTop: '3rem' }}
        >
          <S.MainContainer>
            <Flex justify='space-between'>
              <Flex direction='column' gap='0.5rem'>
                <Text.Medium color={theme.color.dark.secondary_500}>{template.category?.name}</Text.Medium>
                <Heading.Medium color={theme.mode === 'dark' ? theme.color.dark.white : theme.color.light.black}>
                  {template.title}
                </Heading.Medium>
                <Text.Medium color={theme.color.dark.secondary_500}>{template.description}</Text.Medium>
                <Text.Small
                  color={theme.mode === 'dark' ? theme.color.dark.primary_300 : theme.color.light.primary_500}
                  weight='bold'
                >
                  {formatRelativeTime(template.modifiedAt)}
                </Text.Small>
              </Flex>
              <Flex align='center' gap='1rem'>
                <S.EditButton
                  size='small'
                  variant='text'
                  onClick={() => {
                    handleEditButtonClick();
                  }}
                >
                  <img src={pencilIcon} width={24} height={24} alt='Delete snippet' />
                </S.EditButton>
                <S.DeleteButton
                  size='small'
                  variant='text'
                  onClick={() => {
                    handleDelete();
                  }}
                >
                  <img src={trashcanIcon} width={28} height={28} alt='Delete snippet' />
                </S.DeleteButton>
              </Flex>
            </Flex>

            {template.snippets.map((snippet, index) => (
              <div id={snippet.filename} key={snippet.id} ref={(el) => (snippetRefs.current[index] = el)}>
                <Flex
                  align='center'
                  height='3rem'
                  padding='1rem 1.5rem'
                  style={{ background: '#393e46', borderRadius: '8px 8px 0 0' }}
                >
                  <Text.Small color='#fff' weight='bold'>
                    {snippet.filename}
                  </Text.Small>
                </Flex>
                <SyntaxHighlighter
                  language={getLanguageByFilename(snippet.filename)}
                  style={vscDarkPlus}
                  showLineNumbers={true}
                  customStyle={{
                    borderRadius: '0 0 8px 8px',
                    width: '100%',
                    tabSize: 2,
                    margin: 0,
                  }}
                  codeTagProps={{
                    style: {
                      fontSize: '1rem',
                    },
                  }}
                >
                  {snippet.content}
                </SyntaxHighlighter>
              </div>
            ))}
          </S.MainContainer>

          <S.SidebarContainer>
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
          </S.SidebarContainer>
        </Flex>
      )}
    </>
  );
};

export default TemplatePage;
