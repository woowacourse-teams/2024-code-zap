import { Input, LoadingBall } from '@/components';
import { useLoaderDelay } from '@/hooks';
import { theme } from '@design/style/theme';

interface Props {
  value: string;
  onEnterDown: (e: React.KeyboardEvent<HTMLInputElement>) => void;
  onChange: (
    e: React.ChangeEvent<HTMLInputElement>,
    compareValue?: string
  ) => void;
  isPending: boolean;
}

const NewCategoryInput = ({
  value,
  onChange,
  onEnterDown,
  isPending,
}: Props) => {
  const showLoader = useLoaderDelay(isPending, 700);

  return (
    <Input
      size='medium'
      variant='outlined'
      inputColor={theme.color.light.secondary_400}
    >
      {showLoader ? (
        <LoadingBall />
      ) : (
        <Input.TextField
          autoFocus
          placeholder='+ 새 카테고리 생성'
          value={value}
          onChange={onChange}
          onKeyDown={onEnterDown}
          placeholderColor={theme.color.light.secondary_600}
        />
      )}
    </Input>
  );
};

export default NewCategoryInput;
