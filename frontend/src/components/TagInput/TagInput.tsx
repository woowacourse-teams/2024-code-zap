import { xCircle } from '@/assets/images';
import { removeAllWhitespace } from '@/utils/removeAllWhitespace';
import Flex from '../Flex/Flex';
import Input from '../Input/Input';
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
        borderBottom: 'solid 1px #788496',
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
            <img src={xCircle} width={12} height={12} alt='' />
          </S.Tag>
        ))}
      </Flex>
      <Input>
        <Input.TextField
          placeholder='태그를 입력해주세요'
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
