import { Text } from '@/components';
import { theme } from '@/style/theme';

import CategoryName from './CategoryName';
import CategoryNameInput from './CategoryNameInput';
import IconButtons from './IconButtons';

interface ExistingCategoryItemProps {
  id: number;
  name: string;
  isEditing: boolean;
  isDeleted: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: () => void;
  onKeyDown: (e: React.KeyboardEvent) => void;
  onEditClick: (id: number) => void;
  onDeleteClick: (id: number) => void;
  onRestoreClick: (id: number) => void;
}

const ExistingCategoryItem = ({
  id,
  name,
  isEditing,
  isDeleted,
  onChange,
  onBlur,
  onKeyDown,
  onEditClick,
  onDeleteClick,
  onRestoreClick,
}: ExistingCategoryItemProps) => (
  <>
    {isDeleted ? (
      <>
        <CategoryName>
          <Text.Medium color={theme.color.light.analogous_primary_400} textDecoration='line-through'>
            {name}
          </Text.Medium>
        </CategoryName>
        <IconButtons restore onRestoreClick={() => onRestoreClick(id)} />
      </>
    ) : (
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
    )}
  </>
);

export default ExistingCategoryItem;
