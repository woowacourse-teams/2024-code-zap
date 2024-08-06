import { TemplateEdit } from '@/components';
import { useTemplateEdit } from '@/hooks/template/useTemplateEdit';
import type { Template } from '@/types';

interface Props {
  template: Template;
  toggleEditButton: () => void;
}

const TemplateEditPage = ({ template, toggleEditButton }: Props) => {
  const props = useTemplateEdit({ template, toggleEditButton });

  return <TemplateEdit {...props} />;
};

export default TemplateEditPage;
