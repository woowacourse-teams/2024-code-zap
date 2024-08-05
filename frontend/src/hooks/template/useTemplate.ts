import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useScrollToTargetElement } from '../utils/useScrollToTargetElement';
import { useTemplateDeleteQuery } from './useTemplateDeleteQuery';
import { useTemplateQuery } from './useTemplateQuery';

export const useTemplate = (id: number) => {
  const navigate = useNavigate();
  const scrollTo = useScrollToTargetElement();

  const { data: template } = useTemplateQuery(Number(id));
  const { mutateAsync: deleteTemplate } = useTemplateDeleteQuery(Number(id));

  const [currentFile, setCurrentFile] = useState<number | null>(null);
  const [isEdit, setIsEdit] = useState(false);

  const snippetRefs = useRef<(HTMLDivElement | null)[]>([]);

  useEffect(() => {
    if (template && template.snippets.length > 0) {
      setCurrentFile(template.snippets[0].id as number);
    }
  }, [template]);

  const toggleEditButton = () => {
    setIsEdit((prev) => !prev);
  };

  const handleEditButtonClick = () => {
    toggleEditButton();
  };

  const handleSelectOption = (index: number) => (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();

    const targetElement = snippetRefs.current[index];

    scrollTo(targetElement);

    const id = template?.snippets[index].id;

    if (!id) {
      console.error('snippet id가 존재하지 않습니다.');

      return;
    }

    setCurrentFile(() => id);
  };

  const handleDelete = () => {
    deleteTemplate();
    navigate('/');
  };

  return {
    currentFile,
    template,
    isEdit,
    snippetRefs,
    toggleEditButton,
    handleEditButtonClick,
    handleSelectOption,
    handleDelete,
  };
};
