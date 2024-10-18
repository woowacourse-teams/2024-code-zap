import { useState, useRef, useEffect } from 'react';

import { ChevronIcon } from '@/assets/images';
import { TagButton } from '@/components';
import { useToggle, useWindowWidth } from '@/hooks';
import type { Tag } from '@/types';
import { remToPx } from '@/utils';

import * as S from './TagFilterMenu.style';

const LINE_HEIGHT_REM = 1.875;

interface Props {
  tagList: Tag[];
  selectedTagIds: number[];
  onSelectTags: (selectedTagIds: number[]) => void;
}

const TagFilterMenu = ({ tagList, selectedTagIds, onSelectTags }: Props) => {
  const [deselectedTags, setDeselectedTags] = useState<Tag[]>([]);
  const [isTagBoxOpen, toggleTagBox] = useToggle(false);
  const [height, setHeight] = useState(`${LINE_HEIGHT_REM}rem`);
  const containerRef = useRef<HTMLDivElement>(null);
  const [showMoreButton, setShowMoreButton] = useState(false);
  const windowWidth = useWindowWidth();

  useEffect(() => {
    const updateTagContainerState = () => {
      if (containerRef.current) {
        const containerHeight = containerRef.current.scrollHeight;

        setHeight(isTagBoxOpen ? `${containerHeight}px` : `${LINE_HEIGHT_REM}rem`);

        if (containerHeight > remToPx(LINE_HEIGHT_REM)) {
          setShowMoreButton(true);
        } else {
          setShowMoreButton(false);
        }
      }
    };

    updateTagContainerState();
  }, [tagList, selectedTagIds, isTagBoxOpen, windowWidth]);

  const handleButtonClick = (tagId: number) => {
    if (selectedTagIds.includes(tagId)) {
      const deselectedTag = tagList.find((tag) => tag.id === tagId);

      if (deselectedTag) {
        setDeselectedTags((prev) => [deselectedTag, ...prev.filter((tag) => tag.id !== tagId)]);
      }

      onSelectTags(selectedTagIds.filter((id) => id !== tagId));
    } else {
      setDeselectedTags((prev) => prev.filter((tag) => tag.id !== tagId));
      onSelectTags([...selectedTagIds, tagId]);
    }
  };

  const selectedTags = selectedTagIds.map((id) => tagList.find((tag) => tag.id === id)!).filter(Boolean);

  const unselectedTags = deselectedTags.concat(
    tagList.filter(
      (tag) => !selectedTagIds.includes(tag.id) && !deselectedTags.some((deselectedTag) => deselectedTag.id === tag.id),
    ),
  );

  return (
    <S.TagFilterMenuContainer data-testid='tag-filter-menu'>
      <S.TagButtonsContainer ref={containerRef} height={height}>
        {selectedTags.map((tag) => (
          <TagButton key={tag.id} name={tag.name} isFocused={true} onClick={() => handleButtonClick(tag.id)} />
        ))}
        {unselectedTags.map((tag) => (
          <TagButton key={tag.id} name={tag.name} isFocused={false} onClick={() => handleButtonClick(tag.id)} />
        ))}
      </S.TagButtonsContainer>
      {showMoreButton && (
        <S.ShowMoreButton size={LINE_HEIGHT_REM} onClick={toggleTagBox} isExpanded={isTagBoxOpen}>
          <ChevronIcon width={16} height={16} aria-label='태그 더보기' />
        </S.ShowMoreButton>
      )}
    </S.TagFilterMenuContainer>
  );
};

export default TagFilterMenu;
