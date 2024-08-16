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

  const snippetRefs = useRef<(HTMLDivElement | null)[]>([]);

  const [isOpenList, setIsOpenList] = useState<boolean[]>(template?.snippets.map(() => true) || []);

  useEffect(() => {
    if (template && template.snippets.length > 0) {
      setCurrentFile(template.snippets[0].id as number);
      setIsOpenList(template?.snippets.map(() => true));
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

  const handleIsOpenList = (index: number) => () => {
    setIsOpenList((prev) => prev.map((isOpen, idx) => (index === idx ? !isOpen : isOpen)));
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
    isOpenList,
    handleIsOpenList,
  };
};
