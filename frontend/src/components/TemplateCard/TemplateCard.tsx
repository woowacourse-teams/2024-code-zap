import { useNavigate } from 'react-router-dom';

import { ClockIcon, PersonIcon, PrivateIcon } from '@/assets/images';
import { Button, Flex, LikeCounter, TagButton, Text, SourceCodeViewer } from '@/components';
import { useToggle } from '@/hooks';
import { END_POINTS } from '@/routes';
import { VISIBILITY_PRIVATE } from '@/service/constants';
import { ICON_SIZE } from '@/style/styleConstants';
import { theme } from '@/style/theme';
import type { Tag, TemplateListItem } from '@/types';
import { formatRelativeTime } from '@/utils/formatRelativeTime';

import * as S from './TemplateCard.style';

interface Props {
  template: TemplateListItem;
}

const TemplateCard = ({ template }: Props) => {
  const navigate = useNavigate();
  const { title, description, thumbnail, tags, modifiedAt, member, visibility } = template;
  const [showAllTagList, toggleShowAllTagList] = useToggle();
  const isPrivate = visibility === VISIBILITY_PRIVATE;

  const blockMovingToDetailPage = (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent> | React.MouseEvent<HTMLDivElement, MouseEvent>,
  ) => {
    e.preventDefault();
    e.stopPropagation();
  };

  const handleAuthorClick = () => {
    navigate(END_POINTS.memberTemplates(member.id));
  };

  const handleAllTagList = (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent> | React.MouseEvent<HTMLDivElement, MouseEvent>,
  ) => {
    blockMovingToDetailPage(e);
    toggleShowAllTagList();
  };

  return (
    <S.TemplateCardContainer data-testid='template-card'>
      <Flex width='100%' direction='column' gap='1rem'>
        <Flex width='100%' justify='space-between' gap='1rem'>
          <Flex gap='0.75rem' flex='1' style={{ minWidth: '0' }}>
            {isPrivate && <PrivateIcon width={ICON_SIZE.X_SMALL} color={theme.color.light.secondary_600} />}

            <S.AuthorInfoContainer onClick={handleAuthorClick} style={{ cursor: 'pointer' }}>
              <PersonIcon color={theme.color.light.primary_500} />
              <S.EllipsisTextWrapper style={{ width: '100%' }}>
                <Text.Small color={theme.color.light.primary_500}>{member.name}</Text.Small>
              </S.EllipsisTextWrapper>
            </S.AuthorInfoContainer>

            <Flex align='center' gap='0.25rem'>
              <ClockIcon width={ICON_SIZE.X_SMALL} color={theme.color.light.secondary_600} />
              <S.NoWrapTextWrapper>
                <Text.Small color={theme.color.light.secondary_600}>{formatRelativeTime(modifiedAt)}</Text.Small>
              </S.NoWrapTextWrapper>
            </Flex>
          </Flex>
          <Flex align='center' justify='flex-end' flex='0 0 auto'>
            <LikeCounter likesCount={template.likesCount} isLiked={template.isLiked} />
          </Flex>
        </Flex>

        <S.EllipsisTextWrapper>
          <Text.XLarge color={theme.color.light.secondary_900} weight='bold'>
            {title}
          </Text.XLarge>
        </S.EllipsisTextWrapper>

        <S.EllipsisTextWrapper>
          {description ? (
            <Text.Medium color={theme.color.light.secondary_600}>{description}</Text.Medium>
          ) : (
            <S.BlankDescription />
          )}
        </S.EllipsisTextWrapper>
      </Flex>

      <SourceCodeViewer mode='thumbnailView' filename={thumbnail.filename} content={thumbnail.content} />

      <Flex justify='space-between' onClick={blockMovingToDetailPage}>
        <S.TagListContainer>
          {tags.map((tag: Tag) => (
            <Flex key={tag.id}>
              <TagButton id={tag.id} name={tag.name} disabled={true} />
            </Flex>
          ))}
        </S.TagListContainer>
        <Button variant='text' size='small' css={{ whiteSpace: 'nowrap' }} onMouseEnter={handleAllTagList}>
          <Text.XSmall color={tags.length !== 0 ? theme.color.light.secondary_500 : 'transparent'}>
            모든 태그
          </Text.XSmall>
        </Button>
      </Flex>

      <S.AllTagListModal onClick={blockMovingToDetailPage} onMouseLeave={handleAllTagList}>
        {tags.length !== 0 && showAllTagList && (
          <S.AllTagListContainer>
            {tags.map((tag: Tag) => (
              <TagButton key={tag.id} id={tag.id} name={tag.name} disabled={true} />
            ))}
          </S.AllTagListContainer>
        )}
      </S.AllTagListModal>
    </S.TemplateCardContainer>
  );
};

export default TemplateCard;
