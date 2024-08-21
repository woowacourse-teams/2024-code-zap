import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useTemplateDeleteMutation, useTemplateQuery } from '@/queries/template';
import { useScrollToTargetElement } from '../utils';

export const useTemplate = (id: number) => {
  const navigate = useNavigate();
  const scrollTo = useScrollToTargetElement();

  const { data: template } = useTemplateQuery(Number(id));
  const { mutateAsync: deleteTemplate } = useTemplateDeleteMutation([Number(id)]);

  const [currentFile, setCurrentFile] = useState<number | null>(null);
  const [isEdit, setIsEdit] = useState(false);

  const sourceCodeRefs = useRef<(HTMLDivElement | null)[]>([]);

  const [isOpenList, setIsOpenList] = useState<boolean[]>(template?.sourceCodes.map(() => true) || []);

  useEffect(() => {
    if (template && template?.sourceCodes.length > 0) {
      setCurrentFile(template?.sourceCodes[0].id as number);
      setIsOpenList(template?.sourceCodes.map(() => true));
    }
  }, [template]);

  const toggleEditButton = () => {
    setIsEdit((prev) => !prev);
  };

  const handleEditButtonClick = () => {
    toggleEditButton();
  };

  const handleSelectOption = (index: number) => (event?: React.MouseEvent<HTMLAnchorElement>) => {
    event?.preventDefault();

    const targetElement = sourceCodeRefs.current[index];

    if (event) {
      scrollTo(targetElement);
    }

    const id = template?.sourceCodes[index].id;

    if (!id) {
      console.error('sourceCode id가 존재하지 않습니다.');

      return;
    }

    setCurrentFile(() => id);
  };

  const handleDelete = () => {
    deleteTemplate();
    navigate('/');
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
