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
    topic: 'hotTopic.wooteco.topic',
    description: 'hotTopic.wooteco.description',
    subDescription: 'hotTopic.wooteco.subDescription',
    tagIds: [359],
    bg: topicWooteco,
    color: '#FFD269',
  },
  {
    topic: 'hotTopic.precourse.topic',
    description: 'hotTopic.precourse.description',
    subDescription: 'hotTopic.precourse.subDescription',
    tagIds: [364],
    bg: topicPrecourse,
    color: '#F6836C',
  },
  {
    topic: 'hotTopic.javascript.topic',
    description: 'hotTopic.javascript.description',
    subDescription: 'hotTopic.javascript.subDescription',
    tagIds: [41, 211, 329, 351, 249, 360],
    bg: topicJs,
    color: '#C2B12E',
  },
  {
    topic: 'hotTopic.java.topic',
    description: 'hotTopic.java.description',
    subDescription: 'hotTopic.java.subDescription',
    tagIds: [73, 358, 197],
    bg: topicJava,
    color: '#68B7DF',
  },
  {
    topic: 'hotTopic.kotlin.topic',
    description: 'hotTopic.kotlin.description',
    subDescription: 'hotTopic.kotlin.subDescription',
    tagIds: [237, 361, 363],
    bg: topicKt,
    color: '#F08852',
  },
  {
    topic: 'hotTopic.spring.topic',
    description: 'hotTopic.spring.description',
    subDescription: 'hotTopic.spring.subDescription',
    tagIds: [14, 198],
    bg: topicSpring,
    color: '#90C470',
  },
  {
    topic: 'hotTopic.react.topic',
    description: 'hotTopic.react.description',
    subDescription: 'hotTopic.react.subDescription',
    tagIds: [50, 289, 318],
    bg: topicReact,
    color: '#4DC6D9',
  },
  {
    topic: 'hotTopic.android.topic',
    description: 'hotTopic.android.description',
    subDescription: 'hotTopic.android.subDescription',
    tagIds: [236, 287, 362],
    bg: topicAndroid,
    color: '#6BB449',
  },
  {
    topic: 'hotTopic.algorithm.topic',
    description: 'hotTopic.algorithm.description',
    subDescription: 'hotTopic.algorithm.subDescription',
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
