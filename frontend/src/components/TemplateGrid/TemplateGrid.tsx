import { TemplateCard } from '@/components';
import { TemplateListItem } from '@/types';
import * as S from './TemplateGrid.style';

interface Props {
  templates: TemplateListItem[];
  cols?: number;
}

const TemplateGrid = ({ templates, cols = 2 }: Props) => (
  <S.TemplateContainer cols={cols}>
    {templates.map((template) => (
      <TemplateCard key={template.id} template={template} />
    ))}
  </S.TemplateContainer>
);

export default TemplateGrid;
