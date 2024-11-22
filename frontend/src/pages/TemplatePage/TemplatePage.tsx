import { Link, useParams } from 'react-router-dom';

import { ClockIcon, PrivateIcon, ShareIcon } from '@/assets/images';
import {
  AuthorInfo,
  Button,
  Flex,
  Heading,
  LikeButton,
  Modal,
  SelectList,
  SourceCodeViewer,
  TagButton,
  Text,
  NonmemberAlerter,
  LoadingFallback,
} from '@/components';
import { useToggle, useToast } from '@/hooks';
import { useAuth } from '@/hooks/authentication';
import { TemplateEditPage } from '@/pages';
import { useLike, useTemplate } from '@/pages/TemplatePage/hooks';
import { END_POINTS } from '@/routes';
import { useTrackPageViewed } from '@/service/amplitude';
import { trackClickTemplateShare, trackLikeButton } from '@/service/amplitude/track';
import { VISIBILITY_PRIVATE } from '@/service/constants';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import { formatRelativeTime } from '@/utils';

import * as S from './TemplatePage.style';

const TemplatePage = () => {
  const { id } = useParams<{ id: string }>();

  useTrackPageViewed({ eventName: '[Viewed] 템플릿 조회 페이지', eventProps: { templateId: id } });

  const { infoAlert } = useToast();

  const [isNonmemberAlerterOpen, toggleNonmemberAlerter] = useToggle();

  const {
    isLogin,
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

  const isPrivate = template?.visibility === VISIBILITY_PRIVATE;

  const { likesCount, isLiked, toggleLike } = useLike({
    templateId: Number(id),
    initialLikesCount: template?.likesCount || 0,
    initialIsLiked: template?.isLiked || false,
  });

  const handleLikeButtonClick = () => {
    if (!isLogin) {
      toggleNonmemberAlerter();

      return;
    }

    toggleLike();
    trackLikeButton({ isLiked, likesCount, templateId: id as string });
  };

  const handleShareButtonClick = () => {
    const currentUrl = window.location.href;

    navigator.clipboard.writeText(currentUrl);
    infoAlert('붙여넣기로 링크를 공유해보세요');
    trackClickTemplateShare();
  };

  if (!template) {
    return <LoadingFallback />;
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
                <Flex justify='space-between' align='center'>
                  <Text.Medium color={theme.color.dark.secondary_500}>{template.category?.name}</Text.Medium>
                  <Flex gap='0.5rem' justify='flex-end' align='flex-end'>
                    {template.member.name === name && (
                      <>
                        <S.EditButton size='small' variant='text' onClick={handleEditButtonClick}>
                          <Text.Medium color={theme.color.light.secondary_700}>편집</Text.Medium>
                        </S.EditButton>
                        <S.DeleteButton size='small' variant='text' onClick={toggleModal}>
                          <Text.Medium color={theme.color.light.secondary_700}>삭제</Text.Medium>
                        </S.DeleteButton>
                      </>
                    )}
                    <S.ShareButton size='small' variant='text' onClick={handleShareButtonClick}>
                      <Text.Medium color={theme.color.light.secondary_700}>공유</Text.Medium>
                      <ShareIcon width={ICON_SIZE.SMALL} color={theme.color.light.secondary_700} />
                    </S.ShareButton>
                  </Flex>
                </Flex>

                <Flex align='center' justify='space-between' gap='1rem'>
                  <Flex gap='0.5rem'>
                    {isPrivate && (
                      <S.PrivateWrapper>
                        <PrivateIcon width={ICON_SIZE.X_SMALL} />
                      </S.PrivateWrapper>
                    )}
                    <Heading.Large color={theme.mode === 'dark' ? theme.color.dark.white : theme.color.light.black}>
                      {template.title}
                    </Heading.Large>
                  </Flex>
                  <LikeButton likesCount={likesCount} isLiked={isLiked} onLikeButtonClick={handleLikeButtonClick} />
                </Flex>

                <Flex gap='0.5rem' align='center' justify='space-between'>
                  <Link to={END_POINTS.memberTemplates(template.member.id)}>
                    <AuthorInfo memberName={template.member.name} />
                  </Link>

                  <Flex align='center' gap='0.125rem'>
                    <ClockIcon width={ICON_SIZE.SMALL} color={theme.color.light.secondary_600} />
                    <Text.Small color={theme.color.light.secondary_600}>
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
                    <TagButton key={tag.id} id={tag.id} name={tag.name} disabled />
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
                  isSelected={currentFile === index}
                >
                  {sourceCode.filename}
                </SelectList.Option>
              ))}
            </SelectList>
          </S.SidebarContainer>
        </Flex>
      )}

      <NonmemberAlerter isOpen={isNonmemberAlerterOpen} toggleModal={toggleNonmemberAlerter} />
    </>
  );
};

export default TemplatePage;
