import { ChangeEvent, Dispatch, KeyboardEvent, SetStateAction } from 'react';

import { Flex, Input, TagButton, Text } from '@/components';
import { ToastContext } from '@/contexts';
import { useCustomContext } from '@/hooks';
import { validateTagLength } from '@/service/validates';
import { theme } from '@/style/theme';

interface Props {
  value: string;
  handleValue: (e: ChangeEvent<HTMLInputElement>) => void;
  resetValue: () => void;
  tags: string[];
  setTags: Dispatch<SetStateAction<string[]>>;
}

const TagInput = ({ value, handleValue, resetValue, tags, setTags }: Props) => {
  const { failAlert } = useCustomContext(ToastContext);

  const handleSpaceBarAndEnterKeydown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === ' ' || e.key === 'Enter') {
      e.preventDefault();
      addTag();
      resetValue();
    }
  };

  const addTag = () => {
    if (value === '') {
      return;
    }

    if (tags.includes(value)) {
      return;
    }

    setTags((prev) => [...prev, value]);
  };

  const handleTagInput = (e: ChangeEvent<HTMLInputElement>) => {
    const errorMessage = validateTagLength(e.target.value);

    if (errorMessage) {
      failAlert(errorMessage);

      return;
    }

    handleValue(e);
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
          <TagButton
            key={idx}
            id={idx}
            variant='edit'
            name={tag}
            onClick={() => {
              setTags((prev) => prev.filter((el) => el !== tag));
            }}
          />
        ))}
      </Flex>
      <Input size='large' variant='outlined'>
        <Input.TextField
          placeholder='enter 또는 space bar로 태그를 등록해보세요'
          value={value}
          onChange={handleTagInput}
          onKeyUp={handleSpaceBarAndEnterKeydown}
          onBlur={() => {
            addTag();
            resetValue();
          }}
        />
      </Input>
    </Flex>
  );
};

export default TagInput;
