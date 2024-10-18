import { useEffect } from 'react';
import { Link } from 'react-router-dom';

import { TemplateCard } from '@/components';
import { END_POINTS } from '@/routes/endPoints';
import { TemplateListItem } from '@/types';

import * as S from './TemplateGrid.style';

interface Props {
  templateList: TemplateListItem[];
  isEditMode: boolean;
  selectedList: number[];
  setSelectedList: React.Dispatch<React.SetStateAction<number[]>>;
  cols?: number;
}

const TemplateGrid = ({ templateList, isEditMode, selectedList, setSelectedList, cols = 2 }: Props) => {
  useEffect(() => {
    const resetSelectedList = () => {
      setSelectedList([]);
    };

    resetSelectedList();
  }, [isEditMode]);

  const toggleTemplateSelection = (templateId: number) => () => {
    setSelectedList((prev) =>
      prev.includes(templateId) ? prev.filter((id) => id !== templateId) : [...prev, templateId],
    );
  };

  return (
    <S.TemplateContainer cols={cols}>
      {templateList.map((template) =>
        isEditMode ? (
          <S.TemplateCardWrapper
            key={template.id}
            isSelected={selectedList.includes(template.id)}
            onClick={toggleTemplateSelection(template.id)}
          >
            <S.NonInteractiveWrapper>
              <TemplateCard template={template} />
            </S.NonInteractiveWrapper>
          </S.TemplateCardWrapper>
        ) : (
          <Link to={END_POINTS.template(template.id)} key={template.id}>
            <TemplateCard template={template} />
          </Link>
        ),
      )}
    </S.TemplateContainer>
  );
};

export default TemplateGrid;
