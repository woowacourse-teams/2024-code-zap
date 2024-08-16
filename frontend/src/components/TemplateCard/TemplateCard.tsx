import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { oneLight } from 'react-syntax-highlighter/dist/esm/styles/prism';

import { Button, Flex, TagButton, Text } from '@/components';
import { useToggle } from '@/hooks/utils';
import { theme } from '@/style/theme';
import type { Tag, TemplateListItem } from '@/types';
import { getLanguageByFilename } from '@/utils';
import { formatRelativeTime } from '@/utils/formatRelativeTime';
import * as S from './TemplateCard.style';

interface Props {
  template: TemplateListItem;
}

const TemplateCard = ({ template }: Props) => {
  const { title, description, thumbnailSnippet, tags, modifiedAt } = template;
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
      <Flex direction='column' gap='1rem'>
        <Flex justify='space-between' gap='3rem'>
          <S.EllipsisTextWrapper>
            <Text.XLarge color={theme.color.light.secondary_900} weight='bold'>
              {title}
            </Text.XLarge>
          </S.EllipsisTextWrapper>
          <S.NoWrapTextWrapper>
            <Text.XSmall color={theme.color.light.secondary_500}>{formatRelativeTime(modifiedAt)}</Text.XSmall>
          </S.NoWrapTextWrapper>
        </Flex>

        <S.EllipsisTextWrapper>
          <Text.Medium color={theme.color.light.secondary_600}>{description}</Text.Medium>
        </S.EllipsisTextWrapper>
      </Flex>

      <SyntaxHighlighter
        language={getLanguageByFilename(thumbnailSnippet?.filename ?? '')}
        style={oneLight}
        showLineNumbers={true}
        customStyle={{ margin: '1rem 0', borderRadius: '8px', width: '100%', height: '9.5rem', tabSize: 2 }}
        codeTagProps={{
          style: {
            fontSize: '0.875rem',
          },
        }}
      >
        {thumbnailSnippet?.thumbnailContent}
      </SyntaxHighlighter>

      <Flex justify='space-between' onClick={blockMovingToDetailPage}>
        <S.TagListContainer>
          {tags.map((tag: Tag) => (
            <Flex key={tag.id}>
              <TagButton name={tag.name} disabled={true} />
            </Flex>
          ))}
        </S.TagListContainer>
        <Button variant='text' size='small' css={{ whiteSpace: 'nowrap' }} onMouseEnter={handleAllTagList}>
          <Text.XSmall color={theme.color.light.secondary_500}>모든 태그</Text.XSmall>
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
