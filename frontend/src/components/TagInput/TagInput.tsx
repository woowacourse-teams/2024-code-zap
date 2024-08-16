import { XCircleIcon } from '@/assets/images';
import { Flex, Input, Text } from '@/components';
import { theme } from '@/style/theme';
import { removeAllWhitespace } from '@/utils/removeAllWhitespace';
import * as S from './TagInput.style';

interface Props {
  value: string;
  handleValue: (e: React.ChangeEvent<HTMLInputElement>) => void;
  resetValue: () => void;
  tags: string[];
  setTags: React.Dispatch<React.SetStateAction<string[]>>;
}

const TagInput = ({ value, handleValue, resetValue, tags, setTags }: Props) => {
  const handleSpaceBarAndEnterKeydown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === ' ' || e.key === 'Enter') {
      addTag();
      resetValue();
    }
  };

  const addTag = () => {
    if (removeAllWhitespace(value) === '') {
      return;
    }

    if (tags.includes(value)) {
      return;
    }

    setTags((prev) => [...prev, removeAllWhitespace(value)]);
  };

  return (
    <Flex
      justify='center'
      align='center'
      gap='0.5rem'
      css={{
        width: '100%',
        flexWrap: 'wrap',
        marginTop: '1rem',
      }}
    >
      {tags.length !== 0 && (
        <Flex justify='flex-end' width='100%'>
          <Text.XSmall color={theme.color.light.tertiary_400}>
            등록된 태그를 누르면 태그 등록을 쉽게 취소할 수 있어요!
          </Text.XSmall>
        </Flex>
      )}
      <Flex gap='0.25rem' css={{ flexWrap: 'wrap', width: '100%' }}>
        {tags?.map((tag, idx) => (
          <S.Tag
            key={idx}
            onClick={() => {
              setTags((prev) => prev.filter((el) => el !== tag));
            }}
          >
            {tag}
            <XCircleIcon width={16} height={16} aria-label='태그 삭제' />
          </S.Tag>
        ))}
      </Flex>
      <Input size='large'>
        <Input.TextField
          placeholder='enter 또는 space bar로 태그를 등록해보세요'
          value={value}
          onChange={handleValue}
          onKeyUpCapture={handleSpaceBarAndEnterKeydown}
          onBlur={() => {
            resetValue();
          }}
        />
      </Input>
    </Flex>
  );
};

export default TagInput;
