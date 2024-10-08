import { type LanguageName, loadLanguage } from '@uiw/codemirror-extensions-langs';
import { quietlight } from '@uiw/codemirror-theme-quietlight';
import CodeMirror, { EditorView } from '@uiw/react-codemirror';

import { ClockIcon, PersonIcon } from '@/assets/images';
import { Button, Flex, LikeCounter, TagButton, Text } from '@/components';
import { useToggle } from '@/hooks';
import { theme } from '@/style/theme';
import type { Tag, TemplateListItem } from '@/types';
import { getLanguageByFilename } from '@/utils';
import { formatRelativeTime } from '@/utils/formatRelativeTime';

import * as S from './TemplateCard.style';

interface Props {
  template: TemplateListItem;
}

const TemplateCard = ({ template }: Props) => {
  const { title, description, thumbnail, tags, modifiedAt, member } = template;
  const [showAllTagList, toggleShowAllTagList] = useToggle();

  const blockMovingToDetailPage = (
    e: React.MouseEvent<HTMLButtonElement, MouseEvent> | React.MouseEvent<HTMLDivElement, MouseEvent>,
  ) => {
    e.preventDefault();
    e.stopPropagation();
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
            <Flex align='center' gap='0.25rem' style={{ minWidth: '0' }}>
              <PersonIcon width={14} />
              <S.EllipsisTextWrapper style={{ width: '100%' }}>
                <Text.Small
                  color={theme.mode === 'dark' ? theme.color.dark.primary_300 : theme.color.light.primary_500}
                >
                  {member.name}
                </Text.Small>
              </S.EllipsisTextWrapper>
            </Flex>
            <Flex align='center' gap='0.25rem'>
              <ClockIcon width={14} />
              <S.NoWrapTextWrapper>
                <Text.Small color={theme.color.light.primary_500}>{formatRelativeTime(modifiedAt)}</Text.Small>
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

      <CodeMirror
        value={thumbnail?.content}
        height='10rem'
        style={{ width: '100%', fontSize: '1rem', margin: '1rem 0' }}
        theme={quietlight}
        extensions={[
          loadLanguage(getLanguageByFilename(thumbnail?.filename) as LanguageName) || [],
          S.CustomCodeMirrorTheme,
          EditorView.editable.of(false),
        ]}
        css={{
          '.cm-editor': {
            borderRadius: '8px',
            overflow: 'hidden',
          },
          '.cm-scroller': {
            padding: '1rem 0',
            overflowY: 'auto',
            height: '100%',
          },
        }}
      />
      <Flex justify='space-between' onClick={blockMovingToDetailPage}>
        <S.TagListContainer>
          {tags.map((tag: Tag) => (
            <Flex key={tag.id}>
              <TagButton name={tag.name} disabled={true} />
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
              <TagButton key={tag.id} name={tag.name} disabled={true} />
            ))}
          </S.AllTagListContainer>
        )}
      </S.AllTagListModal>
    </S.TemplateCardContainer>
  );
};

export default TemplateCard;
