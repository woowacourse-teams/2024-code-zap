import { Link } from 'react-router-dom';

import { Flex, TagButton, Text } from '@/components';
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
          <Flex direction='column' gap='1.2rem'>
            <Text.Body color='#191D25' weight='bold'>
              {title}
            </Text.Body>
            <Text.Body color='#585D65'>{description}</Text.Body>
          </Flex>

          <Text.Caption color='#393E46'>{formatRelativeTime(modifiedAt)}</Text.Caption>
        </Flex>

        <Flex gap='0.8rem'>
          {tags.map((tag: Tag) => (
            <TagButton key={tag.id} id={tag.id} name={tag.name} disabled={true} />
          ))}
        </Flex>
      </S.TemplateCardContainer>
    </Link>
  );
};

export default TemplateCard;
