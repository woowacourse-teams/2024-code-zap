import { Text } from '@/components';
import { TAG_COLORS } from '@/style/tagColors';
import { theme } from '@/style/theme';

import { Carousel } from '../';
import * as S from './HotTopicCarousel.style';

const HOT_TOPIC = [
  { key: 1, content: '우테코', tagIds: [359], color: TAG_COLORS[3] },
  { key: 2, content: '프리코스', tagIds: [364], color: TAG_COLORS[4] },
  { key: 4, content: '자바스크립트', tagIds: [41, 211, 329, 351, 249, 360], color: TAG_COLORS[0] },
  { key: 3, content: '자바', tagIds: [73, 358, 197], color: TAG_COLORS[1] },
  { key: 5, content: '코틀린', tagIds: [361, 363], color: TAG_COLORS[2] },
  { key: 8, content: '스프링', tagIds: [14, 198], color: TAG_COLORS[7] },
  { key: 7, content: '리액트', tagIds: [50, 289, 318], color: TAG_COLORS[6] },
  { key: 8, content: '안드로이드', tagIds: [362], color: TAG_COLORS[8] },
  { key: 9, content: '알고리즘', tagIds: [261, 316, 253, 144, 143], color: TAG_COLORS[5] },
];

interface Props {
  selectTopic: ({ tagIds, content }: { tagIds: number[]; content: string }) => void;
  selectedHotTopic: string;
}

const HotTopicCarousel = ({ selectTopic, selectedHotTopic }: Props) => (
  <Carousel
    items={HOT_TOPIC.map(({ key, content, tagIds, color }) => ({
      id: key,
      content: (
        <S.Topic
          isSelected={selectedHotTopic === content}
          background={color.background}
          border={color.border}
          onClick={() => {
            selectTopic({ tagIds, content });
          }}
        >
          <Text.Large color={theme.color.light.secondary_800} weight='bold'>
            {content}
          </Text.Large>
        </S.Topic>
      ),
    }))}
  />
);

export default HotTopicCarousel;
