import { useTagListQuery } from '@/queries/tags';

import { TagFilterMenu } from '../';

interface Props {
  selectedTagIds: number[];
  handleTagMenuClick: (selectedTagIds: number[]) => void;
}

const TagListSection = ({ selectedTagIds, handleTagMenuClick }: Props) => {
  const { data: tagData } = useTagListQuery();
  const tagList = tagData?.tags || [];

  return (
    <section>
      {tagList.length !== 0 && (
        <TagFilterMenu tagList={tagList} selectedTagIds={selectedTagIds} onSelectTags={handleTagMenuClick} />
      )}
    </section>
  );
};

export default TagListSection;
