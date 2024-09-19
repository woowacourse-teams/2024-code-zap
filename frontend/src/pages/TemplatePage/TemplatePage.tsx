import { useTheme } from '@emotion/react';
import { useParams } from 'react-router-dom';

import { ClockIcon, PencilIcon, PersonIcon, TrashcanIcon } from '@/assets/images';
import { Button, Flex, Heading, LikeButton, Modal, SelectList, SourceCodeViewer, TagButton, Text } from '@/components';
import { useToggle } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { TemplateEditPage } from '@/pages';
import { formatRelativeTime } from '@/utils';

import { useTemplate } from './hooks';
import { useLike } from './hooks/useLike';
import * as S from './TemplatePage.style';

const TemplatePage = () => {
  const { id } = useParams<{ id: string }>();
  const theme = useTheme();
  const {
    memberInfo: { name },
  } = useAuth();

  const [isOpen, toggleModal] = useToggle();

  const {
    currentFile,
    template,
    isEdit,
    sourceCodeRefs,
    toggleEditButton,
    handleEditButtonClick,
    handleSelectOption,
    handleDelete,
  } = useTemplate(Number(id));

  const { likesCount, isLiked, toggleLike } = useLike({
    templateId: Number(id),
    initialLikesCount: template?.likesCount || 0,
    initialIsLiked: template?.isLiked || false,
  });

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
                  {template.member.name === name && (
                    <Flex gap='1rem'>
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
                  )}
                </Flex>

                <Flex align='center' justify='space-between' gap='1rem'>
                  <Heading.Large color={theme.mode === 'dark' ? theme.color.dark.white : theme.color.light.black}>
                    {template.title}
                  </Heading.Large>
                  <LikeButton likesCount={likesCount} isLiked={isLiked} onLikeButtonClick={toggleLike} />
                </Flex>

                <Flex gap='0.5rem' align='center'>
                  <Flex align='center' gap='0.125rem'>
                    <PersonIcon width={14} />
                    <Text.Small
                      color={theme.mode === 'dark' ? theme.color.dark.primary_300 : theme.color.light.primary_500}
                    >
                      {template.member.name}
                    </Text.Small>
                  </Flex>
                  <Flex align='center' gap='0.125rem'>
                    <ClockIcon width={16} />
                    <Text.Small
                      color={theme.mode === 'dark' ? theme.color.dark.primary_300 : theme.color.light.primary_500}
                    >
                      {formatRelativeTime(template.modifiedAt)}
                    </Text.Small>
                    <Text.Small
                      color={theme.mode === 'dark' ? theme.color.dark.secondary_300 : theme.color.light.secondary_400}
                    >
                      ({formatRelativeTime(template.createdAt)})
                    </Text.Small>
                  </Flex>
                </Flex>

                <Flex gap='0.25rem' wrap='wrap'>
                  {template.tags.map((tag) => (
                    <TagButton key={tag.id} name={tag.name} disabled></TagButton>
                  ))}
                </Flex>
                <div
                  css={{
                    width: '100%',
                    borderTop: `1px solid ${theme.color.light.secondary_100}`,
                    margin: '0.5rem 0rem',
                  }}
                ></div>
                <Text.Medium color={theme.color.dark.secondary_600}>{template.description}</Text.Medium>
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
              <SourceCodeViewer
                key={sourceCode.id}
                mode='detailView'
                filename={sourceCode.filename}
                content={sourceCode.content}
                sourceCodeRef={(el) => (sourceCodeRefs.current[index] = el)}
              />
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
