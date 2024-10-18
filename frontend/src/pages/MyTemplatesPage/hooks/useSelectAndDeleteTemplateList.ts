import { useState } from 'react';

import { useToggle } from '@/hooks';
import { useTemplateDeleteMutation } from '@/queries/templates';
import { TemplateListItem } from '@/types';

interface Props {
  templateList: TemplateListItem[];
}

export const useSelectAndDeleteTemplateList = ({ templateList }: Props) => {
  const [isEditMode, toggleIsEditMode] = useToggle();
  const [selectedList, setSelectedList] = useState<number[]>([]);
  const [isDeleteModalOpen, toggleDeleteModal] = useToggle();

  const { mutateAsync: deleteTemplates } = useTemplateDeleteMutation(selectedList);

  const handleAllSelected = () => {
    if (selectedList.length === templateList.length) {
      setSelectedList([]);

      return;
    }

    setSelectedList(templateList.map((template) => template.id));
  };

  const handleDelete = () => {
    deleteTemplates();
    toggleIsEditMode();
    toggleDeleteModal();
  };

  return {
    isEditMode,
    toggleIsEditMode,
    isDeleteModalOpen,
    toggleDeleteModal,
    selectedList,
    setSelectedList,
    handleAllSelected,
    handleDelete,
  };
};
