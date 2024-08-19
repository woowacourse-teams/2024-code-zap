import { useTheme } from '@emotion/react';
import { useParams } from 'react-router-dom';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { oneLight } from 'react-syntax-highlighter/dist/esm/styles/prism';

import { ChevronIcon, PencilIcon, TrashcanIcon } from '@/assets/images';
import { Button, Flex, Heading, Modal, SelectList, TagButton, Text } from '@/components';
import { ToastContext } from '@/contexts';
import { useTemplate } from '@/hooks/template';
import { useCustomContext, useToggle } from '@/hooks/utils';
import { TemplateEditPage } from '@/pages';
import type { SourceCodes } from '@/types';
import { formatRelativeTime, getLanguageByFilename } from '@/utils';
import * as S from './TemplatePage.style';

const TemplatePage = () => {
  const { id } = useParams<{ id: string }>();
  const theme = useTheme();

  const { infoAlert } = useCustomContext(ToastContext);
  const [isOpen, toggleModal] = useToggle();

  const copyCode = (sourceCode: SourceCodes) => () => {
    navigator.clipboard.writeText(sourceCode.content);
    infoAlert('코드가 복사되었습니다!');
  };

  const {
    currentFile,
    template,
    isEdit,
    sourceCodeRefs,
    toggleEditButton,
    handleEditButtonClick,
    handleSelectOption,
    handleDelete,
    isOpenList,
    handleIsOpenList,
  } = useTemplate(Number(id));

  if (!template) {
    return <div>템플릿을 불러오는 중...</div>;
  }

  return (
    <>
      {isEdit ? (
        <TemplateEditPage template={template} toggleEditButton={toggleEditButton} />
      ) : (
        <Flex justify='space-between' align='center' width='100%'>
          <S.MainContainer>
            <Flex
              justify='space-between'
              gap='1rem'
              width='100%'
              css={{
                wordBreak: 'break-word',
                overflowWrap: 'break-word',
                whiteSpace: 'normal',
                lineHeight: 'normal',
              }}
            >
              <Flex direction='column' gap='0.75rem' width='100%'>
                <Flex justify='space-between'>
                  <Text.Medium color={theme.color.dark.secondary_500}>{template.category?.name}</Text.Medium>
                  <Flex justify='flex-end'>
                    <S.EditButton
                      size='small'
                      variant='text'
                      onClick={() => {
                        handleEditButtonClick();
                      }}
                    >
                      <PencilIcon width={28} height={28} aria-label='템플릿 편집' />
                    </S.EditButton>
                    <S.DeleteButton size='small' variant='text' onClick={toggleModal}>
                      <TrashcanIcon aria-label='템플릿 삭제' />
                    </S.DeleteButton>
                  </Flex>
                </Flex>

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
                <Flex gap='0.25rem' wrap='wrap'>
                  {template.tags.map((tag) => (
                    <TagButton key={tag.id} name={tag.name}></TagButton>
                  ))}
                </Flex>
              </Flex>
            </Flex>

            {isOpen && (
              <Modal isOpen={isOpen} toggleModal={toggleModal} size='xsmall'>
                <Flex direction='column' justify='space-between' align='center' margin='1rem 0 0 0' gap='2rem'>
                  <Flex direction='column' justify='center' align='center' gap='0.75rem'>
                    <Text.Large color='black' weight='bold'>
                      정말 삭제하시겠습니까?
                    </Text.Large>
                    <Text.Medium color='black'>삭제된 템플릿은 복구할 수 없습니다.</Text.Medium>
                  </Flex>
                  <Flex justify='center' align='center' gap='0.5rem'>
                    <Button variant='outlined' onClick={toggleModal}>
                      취소
                    </Button>
                    <Button onClick={handleDelete}>삭제</Button>
                  </Flex>
                </Flex>
              </Modal>
            )}

            {template.sourceCodes.map((sourceCode, index) => (
              <div id={sourceCode.filename} key={sourceCode.id} ref={(el) => (sourceCodeRefs.current[index] = el)}>
                <Flex
                  justify='space-between'
                  align='center'
                  height='3rem'
                  padding='1rem 1.5rem'
                  style={{ background: `${theme.color.light.tertiary_600}`, borderRadius: '8px 8px 0 0' }}
                >
                  <Flex align='center' gap='0.5rem' onClick={handleIsOpenList(index)} css={{ cursor: 'pointer' }}>
                    <ChevronIcon
                      width={16}
                      height={16}
                      aria-label='소스코드 펼침'
                      css={{
                        transition: 'transform 0.3s ease',
                        transform: isOpenList[index] ? 'rotate(180deg)' : 'rotate(0deg)',
                      }}
                    />
                    <Text.Small color='#fff' weight='bold'>
                      {sourceCode.filename}
                    </Text.Small>
                  </Flex>
                  <Button size='small' variant='text' onClick={copyCode(sourceCode)}>
                    <Text.Small color={theme.color.light.primary_500} weight='bold'>
                      {'복사'}
                    </Text.Small>
                  </Button>
                </Flex>
                <S.SyntaxHighlighterWrapper isOpen={isOpenList[index]}>
                  {isOpenList[index] && (
                    <SyntaxHighlighter
                      language={getLanguageByFilename(sourceCode.filename)}
                      style={oneLight}
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
                      {sourceCode.content}
                    </SyntaxHighlighter>
                  )}
                </S.SyntaxHighlighterWrapper>
              </div>
            ))}
          </S.MainContainer>

          <S.SidebarContainer>
            <SelectList>
              {template.sourceCodes.map((sourceCode, index) => (
                <SelectList.Option
                  key={sourceCode.id}
                  onClick={handleSelectOption(index)}
                  isSelected={currentFile === sourceCode.id}
                >
                  {sourceCode.filename}
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
