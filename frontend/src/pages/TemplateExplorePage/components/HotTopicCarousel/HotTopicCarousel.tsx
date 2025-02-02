import { Text } from '@/components';
import { useWindowWidth } from '@/hooks';
import { HOT_TOPIC } from '@/service/hotTopic';
import { BREAKING_POINT } from '@/style/styleConstants';
import { theme } from '@design/style/theme';

import { Carousel } from '../';
import * as S from './HotTopicCarousel.style';

interface Props {
  selectTopic: ({ tagIds, topic }: { tagIds: number[]; topic: string }) => void;
  selectedHotTopic: string;
}

const HotTopicCarousel = ({ selectTopic, selectedHotTopic }: Props) => {
  const windowWidth = useWindowWidth();
  const isMobile = windowWidth <= BREAKING_POINT.MOBILE;

  return (
    <Carousel
      items={HOT_TOPIC.map(({ topic, description, subDescription, tagIds, color, bg }) => ({
        id: topic,
        content: (
          <S.Topic
            isSelected={selectedHotTopic === topic}
            background={bg}
            border={color}
            onClick={() => {
              selectTopic({ tagIds, topic });
            }}
          >
            <S.Content>
              <S.Title>
                <Text.Large color={theme.color.light.secondary_800} weight='bold'>
                  {topic}
                </Text.Large>
              </S.Title>
              {!isMobile && (
                <S.Description>
                  <Text.Small color={theme.color.light.secondary_800} weight='bold'>
                    {description}
                  </Text.Small>
                  <Text.Small color={theme.color.light.secondary_800}>{subDescription}</Text.Small>
                </S.Description>
              )}
            </S.Content>
          </S.Topic>
        ),
      }))}
    />
  );
};

export default HotTopicCarousel;
