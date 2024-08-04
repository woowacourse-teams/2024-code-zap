import { Link } from 'react-router-dom';

import { Flex, TagButton, Text } from '@/components';
import { theme } from '@/style/theme';
import { Tag, Template } from '@/types/template';
import { formatRelativeTime } from '@/utils/formatRelativeTime';
import * as S from './TemplateCard.style';

interface Props {
  template: Template;
}

const TemplateCard = ({ template }: Props) => {
  const { id, title, description, tags, modifiedAt } = template;

  return (
    <Link to={`/templates/${id}`}>
      <S.TemplateCardContainer>
        <Flex justify='space-between' width='100%'>
          <Flex direction='column' gap='.5rem'>
            <Text.XLarge color={theme.color.light.secondary_900} weight='bold'>
              {title}
            </Text.XLarge>
            <Text.Medium color={theme.color.light.secondary_600}>{description}</Text.Medium>
          </Flex>

          <Text.XSmall color={theme.color.light.secondary_500}>{formatRelativeTime(modifiedAt)}</Text.XSmall>
        </Flex>

        <Flex gap='.5rem'>
          {tags.map((tag: Tag) => (
            <TagButton key={tag.id} id={tag.id} name={tag.name} disabled={true} />
          ))}
        </Flex>
      </S.TemplateCardContainer>
    </Link>
  );
};

export default TemplateCard;
