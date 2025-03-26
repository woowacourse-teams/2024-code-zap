import topicAlgorithm from '@/assets/images/topic_algorithm.jpg';
import topicAndroid from '@/assets/images/topic_android.jpg';
import topicJava from '@/assets/images/topic_java.jpg';
import topicJs from '@/assets/images/topic_js.jpg';
import topicKt from '@/assets/images/topic_kt.jpg';
import topicPrecourse from '@/assets/images/topic_precourse.jpg';
import topicReact from '@/assets/images/topic_react.jpg';
import topicSpring from '@/assets/images/topic_spring.jpg';
import topicWooteco from '@/assets/images/topic_wooteco.jpg';

export const HOT_TOPIC = [
  {
    topic: 'wooteco.topic',
    description: 'wooteco.description',
    subDescription: 'wooteco.subDescription',
    tagIds: [359],
    bg: topicWooteco,
    color: '#FFD269',
  },
  {
    topic: 'precourse.topic',
    description: 'precourse.description',
    subDescription: 'precourse.subDescription',
    tagIds: [364],
    bg: topicPrecourse,
    color: '#F6836C',
  },
  {
    topic: 'javascript.topic',
    description: 'javascript.description',
    subDescription: 'javascript.subDescription',
    tagIds: [41, 211, 329, 351, 249, 360],
    bg: topicJs,
    color: '#C2B12E',
  },
  {
    topic: 'java.topic',
    description: 'java.description',
    subDescription: 'java.subDescription',
    tagIds: [73, 358, 197],
    bg: topicJava,
    color: '#68B7DF',
  },
  {
    topic: 'kotlin.topic',
    description: 'kotlin.description',
    subDescription: 'kotlin.subDescription',
    tagIds: [237, 361, 363],
    bg: topicKt,
    color: '#F08852',
  },
  {
    topic: 'spring.topic',
    description: 'spring.description',
    subDescription: 'spring.subDescription',
    tagIds: [14, 198],
    bg: topicSpring,
    color: '#90C470',
  },
  {
    topic: 'react.topic',
    description: 'react.description',
    subDescription: 'react.subDescription',
    tagIds: [50, 289, 318],
    bg: topicReact,
    color: '#4DC6D9',
  },
  {
    topic: 'android.topic',
    description: 'android.description',
    subDescription: 'android.subDescription',
    tagIds: [236, 287, 362],
    bg: topicAndroid,
    color: '#6BB449',
  },
  {
    topic: 'algorithm.topic',
    description: 'algorithm.description',
    subDescription: 'algorithm.subDescription',
    tagIds: [261, 316, 253, 144, 143],
    bg: topicAlgorithm,
    color: '#D0BB48',
  },
];

export const getHotTopicContent = (tagIds: number[]) => {
  if (!tagIds.length) {
    return '';
  }

  const tagId = tagIds[0];
  const selected = HOT_TOPIC.find((el) => el.tagIds.includes(tagId));

  return selected?.topic || '';
};
