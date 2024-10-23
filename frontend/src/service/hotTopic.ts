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
    topic: '우테코',
    description: '현장 경험과 체계적인 교육의 만남,',
    subDescription: '혼자가 아닌 함께 성장하는 즐거움',
    tagIds: [359],
    bg: topicWooteco,
    color: '#FFD269',
  },
  {
    topic: '프리코스',
    description: '우테코를 향한 첫걸음, ',
    subDescription: '4주간의 성장 여정',
    tagIds: [364],
    bg: topicPrecourse,
    color: '#F6836C',
  },
  {
    topic: '자바스크립트',
    description: '웹의 심장, ',
    subDescription: '브라우저를 넘어 전 영역을 아우르는 통합 언어',
    tagIds: [41, 211, 329, 351, 249, 360],
    bg: topicJs,
    color: '#C2B12E',
  },
  {
    topic: '자바',
    description: '엔터프라이즈의 강자, ',
    subDescription: '20년 이상 신뢰받아온 안정성의 상징',
    tagIds: [73, 358, 197],
    bg: topicJava,
    color: '#68B7DF',
  },
  {
    topic: '코틀린',
    description: '모던 JVM 언어의 결정체,',
    subDescription: '생산성과 안정성의 완벽한 조화',
    tagIds: [237, 361, 363],
    bg: topicKt,
    color: '#F08852',
  },
  {
    topic: '스프링',
    description: '자바 프레임워크의 절대 강자,',
    subDescription: '우아한 서버 아키텍처',
    tagIds: [14, 198],
    bg: topicSpring,
    color: '#90C470',
  },
  {
    topic: '리액트',
    description: '재사용성과 생태계의 힘,',
    subDescription: '현대 웹 개발의 중심',
    tagIds: [50, 289, 318],
    bg: topicReact,
    color: '#4DC6D9',
  },
  {
    topic: '안드로이드',
    description: '모바일 혁명의 주역,',
    subDescription: '전세계 70억 기기의 선택',
    tagIds: [236, 287, 362],
    bg: topicAndroid,
    color: '#6BB449',
  },
  {
    topic: '알고리즘',
    description: '개발자의 필수 교양,',
    subDescription: '효율적 사고의 기초',
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
