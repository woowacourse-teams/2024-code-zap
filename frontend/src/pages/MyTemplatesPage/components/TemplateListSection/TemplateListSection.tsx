import { NoResults } from '@/components';
import { useWindowWidth } from '@/hooks';
import { NewTemplateButton, TemplateGrid } from '@/pages/MyTemplatesPage/components';
import { TemplateListItem } from '@/types';

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
    return isSearching ? <NoResults>검색 결과가 없습니다.</NoResults> : <NewTemplateButton />;
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
