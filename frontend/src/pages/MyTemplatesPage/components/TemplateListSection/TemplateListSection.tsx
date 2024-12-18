import { NoResults } from '@/components';
import { useWindowWidth } from '@/hooks';
import { NewTemplateButton, TemplateGrid } from '@/pages/MyTemplatesPage/components';
import { TemplateListItem } from '@/types';

interface Props {
  templateList: TemplateListItem[];
  isEditMode: boolean;
  isSearching: boolean;
  isMine: boolean;
  selectedList: number[];
  setSelectedList: React.Dispatch<React.SetStateAction<number[]>>;
}

const getGridCols = (windowWidth: number) => (windowWidth <= 1024 ? 1 : 2);

const TemplateListSection = ({ templateList, isSearching, isEditMode, isMine, selectedList, setSelectedList }: Props) => {
  const windowWidth = useWindowWidth();

  if (templateList.length === 0) {
    if (!isMine || isSearching) return <NoResults>검색 결과가 없습니다.</NoResults>
    return <NewTemplateButton />;
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
