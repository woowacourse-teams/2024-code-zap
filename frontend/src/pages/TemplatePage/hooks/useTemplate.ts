import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useSelectList } from '@/hooks';
import { useTemplateDeleteMutation, useTemplateQuery } from '@/queries/templates';
import { END_POINTS } from '@/routes';

export const useTemplate = (id: number) => {
  const navigate = useNavigate();

  const { data: template } = useTemplateQuery(Number(id));
  const { mutateAsync: deleteTemplate } = useTemplateDeleteMutation([Number(id)]);
  const { currentOption: currentFile, linkedElementRefs: sourceCodeRefs, handleSelectOption } = useSelectList();

  const [isEdit, setIsEdit] = useState(false);

  const [isOpenList, setIsOpenList] = useState<boolean[]>(template?.sourceCodes.map(() => true) || []);

  useEffect(() => {
    if (template && template?.sourceCodes.length > 0) {
      setIsOpenList(template?.sourceCodes.map(() => true));
    }
  }, [template]);

  const toggleEditButton = () => {
    setIsEdit((prev) => !prev);
  };

  const handleEditButtonClick = () => {
    toggleEditButton();
  };

  const handleDelete = () => {
    deleteTemplate();
    navigate(END_POINTS.MY_TEMPLATES);
  };

  const handleIsOpenList = (index: number) => () => {
    setIsOpenList((prev) => prev.map((isOpen, idx) => (index === idx ? !isOpen : isOpen)));
  };

  return {
    currentFile,
    template,
    isEdit,
    sourceCodeRefs,
    toggleEditButton,
    handleEditButtonClick,
    handleSelectOption,
    handleDelete,
    isOpenList,
    handleIsOpenList,
  };
};
