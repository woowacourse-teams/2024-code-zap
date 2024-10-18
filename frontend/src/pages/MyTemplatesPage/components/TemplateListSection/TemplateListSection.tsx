import { NoSearchResults } from '@/components';
import { useWindowWidth } from '@/hooks';
import { TemplateListItem } from '@/types';

import { NewTemplateButton, TemplateGrid } from '../';

interface Props {
  templateList: TemplateListItem[];
  isEditMode: boolean;
  isSearching: boolean;
  selectedList: number[];
  setSelectedList: React.Dispatch<React.SetStateAction<number[]>>;
}

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateListSection = ({ templateList, isSearching, isEditMode, selectedList, setSelectedList }: Props) => {
  const windowWidth = useWindowWidth();

  if (templateList.length === 0) {
    return isSearching ? <NoSearchResults /> : <NewTemplateButton />;
  }

  return (
    <TemplateGrid
      templateList={templateList}
      cols={getGridCols(windowWidth)}
      isEditMode={isEditMode}
      selectedList={selectedList}
      setSelectedList={setSelectedList}
    />
  );
};

export default TemplateListSection;
