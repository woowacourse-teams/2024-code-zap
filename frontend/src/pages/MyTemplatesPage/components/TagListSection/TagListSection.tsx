import { useTagListQuery } from '@/queries/tags';

import { TagFilterMenu } from '../';

interface Props {
  selectedTagIds: number[];
  handleTagMenuClick: (selectedTagIds: number[]) => void;
  memberId?: number;
}

const TagListSection = ({ selectedTagIds, handleTagMenuClick, memberId }: Props) => {
  const { data: tagData } = useTagListQuery({ memberId });
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
