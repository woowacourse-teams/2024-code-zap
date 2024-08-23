import { TagButton } from '@/components';
import type { Tag } from '@/types';
import * as S from './TagFilterMenu.style';

interface Props {
  tags: Tag[];
  selectedTagIds: number[];
  onSelectTags: (selectedTagIds: number[]) => void;
}

const TagFilterMenu = ({ tags, selectedTagIds, onSelectTags }: Props) => {
  const handleButtonClick = (tagId: number) => {
    if (selectedTagIds.includes(tagId)) {
      onSelectTags(selectedTagIds.filter((id) => id !== tagId));
    } else {
      onSelectTags([...selectedTagIds, tagId]);
    }
  };

  return (
    <S.TagFilterMenuContainer data-testid='tag-filter-menu'>
      {tags.map((tag) => (
        <TagButton
          key={tag.id}
          name={tag.name}
          isFocused={selectedTagIds.includes(tag.id)}
          onClick={() => handleButtonClick(tag.id)}
        />
      ))}
    </S.TagFilterMenuContainer>
  );
};

export default TagFilterMenu;
