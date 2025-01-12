import { Text } from '@/components';
import { theme } from '@/style/theme';

import CategoryName from './CategoryName';
import CategoryNameInput from './CategoryNameInput';
import IconButtons from './IconButtons';

interface NewCategoryItemProps {
  id: number;
  name: string;
  isEditing: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: () => void;
  onKeyDown: (e: React.KeyboardEvent) => void;
  onEditClick: (id: number) => void;
  onDeleteClick: (id: number) => void;
}

const NewCategoryItem = ({
  id,
  name,
  isEditing,
  onChange,
  onBlur,
  onKeyDown,
  onEditClick,
  onDeleteClick,
}: NewCategoryItemProps) => (
  <>
    <CategoryName>
      {isEditing ? (
        <CategoryNameInput value={name} onChange={onChange} onBlur={onBlur} onKeyDown={onKeyDown} />
      ) : (
        <Text.Medium color={theme.color.light.secondary_500} weight='bold'>
          {name}
        </Text.Medium>
      )}
    </CategoryName>
    <IconButtons edit delete onEditClick={() => onEditClick(id)} onDeleteClick={() => onDeleteClick(id)} />
  </>
);

export default NewCategoryItem;
