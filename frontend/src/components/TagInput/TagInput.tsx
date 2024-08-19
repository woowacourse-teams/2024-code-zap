import { XCircleIcon } from '@/assets/images';
import { Flex, Input } from '@/components';
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

    setTags((prev) => [...prev, removeAllWhitespace(value)]);
  };

  return (
    <Flex
      justify='center'
      align='center'
      css={{
        width: '100%',
        flexWrap: 'wrap',
        marginTop: '1rem',
      }}
    >
      <Flex gap='0.125rem' css={{ flexWrap: 'wrap', width: '100%' }}>
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
      <Input>
        <Input.TextField
          placeholder='enter 또는 space bar로 태그를 등록해보세요'
          value={value}
          onChange={handleValue}
          onKeyUpCapture={handleSpaceBarAndEnterKeydown}
          onBlur={() => resetValue()}
        />
      </Input>
    </Flex>
  );
};

export default TagInput;
