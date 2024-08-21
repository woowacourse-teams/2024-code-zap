import { MouseEvent, useEffect } from 'react';

import { PlusIcon, TrashcanIcon } from '@/assets/images';
import { Button, Dropdown, Flex, Input, SourceCodeEditor, TagInput, Text, Guide, SelectList } from '@/components';
import { useSelectList } from '@/hooks/utils';
import { useCategoryUploadMutation } from '@/queries/category';
import { theme } from '@/style/theme';
import type { Category, SourceCodes } from '@/types';
import * as S from './TemplateEdit.style';

interface Props {
  title: string;
  description: string;
  sourceCodes: SourceCodes[];
  categoryProps: {
    options: Category[];
    isOpen: boolean;
    toggleDropdown: () => void;
    currentValue: Category;
    handleCurrentValue: (newValue: Category) => void;
    getOptionLabel: (category: Category) => string;
    dropdownRef: React.MutableRefObject<HTMLDivElement | null>;
  };
  tagProps: {
    tags: string[];
    setTags: React.Dispatch<React.SetStateAction<string[]>>;
    value: string;
    handleValue: (e: React.ChangeEvent<HTMLInputElement>) => void;
    resetValue: () => void;
  };
  handleTitleChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleDescriptionChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  handleAddButtonClick: () => void;
  handleCancelButton: () => void;
  handleCodeChange: (newContent: string, idx: number) => void;
  handleFileNameChange: (newFileName: string, idx: number) => void;
  handleDeleteSourceCode: (index: number) => void;
  handleSaveButtonClick: () => Promise<void>;
  error: Error | null;
}

const TemplateEdit = ({
  title,
  description,
  sourceCodes,
  tagProps,
  categoryProps,
  handleTitleChange,
  handleDescriptionChange,
  handleAddButtonClick,
  handleCancelButton,
  handleCodeChange,
  handleFileNameChange,
  handleDeleteSourceCode: handleDeleteSourceCode,
  handleSaveButtonClick,
  error,
}: Props) => {
  const { mutateAsync: postCategory, isPending } = useCategoryUploadMutation(categoryProps.handleCurrentValue);
  const { currentFile, setCurrentFile, sourceCodeRefs, handleSelectOption } = useSelectList(sourceCodes);

  const getExistingCategory = (value: string) =>
    categoryProps.options.find((category) => categoryProps.getOptionLabel(category) === value);

  const createNewCategory = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (!(e.target instanceof HTMLInputElement) || e.key !== 'Enter') {
      return;
    }

    const inputValue = e.target.value;
    const existingCategory = getExistingCategory(inputValue);

    if (inputValue.trim() === '') {
      e.target.value = '';

      return;
    }

    if (existingCategory) {
      categoryProps.handleCurrentValue(existingCategory);

      return;
    }

    const newCategory = { name: inputValue };

    postCategory(newCategory);

    e.target.value = '';
  };

  const handleSelectList = (index: number) => (e: MouseEvent<HTMLAnchorElement>) => {
    e.preventDefault();

    handleSelectOption(index)(e);
    setCurrentFile(index);
  };

  useEffect(() => {
    if (sourceCodes.length === 1) {
      setCurrentFile(0);
    }
  }, [sourceCodes, setCurrentFile]);

  return (
    <Flex justify='space-between' align='center' width='100%'>
      <S.TemplateEditContainer>
        <Flex direction='column' justify='center' align='flex-start' gap='1rem' width='100%'>
          <CategoryGuide isOpen={categoryProps.isOpen} isPending={isPending} />
          <Dropdown
            {...categoryProps}
            replaceChildrenWhenIsOpen={<NewCategoryInput createNewCategory={createNewCategory} />}
          />

          <S.UnderlineInputWrapper>
            <Input size='xlarge' variant='text'>
              <Input.TextField placeholder='제목을 입력해주세요' value={title} onChange={handleTitleChange} />
            </Input>
          </S.UnderlineInputWrapper>

          <Input size='large' variant='text'>
            <Input.TextField placeholder='설명을 입력해주세요' value={description} onChange={handleDescriptionChange} />
          </Input>

          {sourceCodes.map((sourceCode, index) => (
            <Flex key={index} style={{ position: 'relative' }} width='100%'>
              <div ref={(el) => (sourceCodeRefs.current[index] = el)} css={{ width: '100%' }}>
                <SourceCodeEditor
                  key={index}
                  index={index}
                  fileName={sourceCode.filename}
                  content={sourceCode.content}
                  onChangeContent={(newContent) => handleCodeChange(newContent, index)}
                  onChangeFileName={(newFileName) => handleFileNameChange(newFileName, index)}
                />
                <S.DeleteButton
                  size='small'
                  variant='text'
                  onClick={() => {
                    handleDeleteSourceCode(index);
                  }}
                >
                  <TrashcanIcon width={24} height={24} aria-label='템플릿 삭제' />
                </S.DeleteButton>
              </div>
            </Flex>
          ))}
          <Button
            size='medium'
            variant='contained'
            buttonColor={theme.color.light.primary_50}
            fullWidth
            onClick={handleAddButtonClick}
          >
            <PlusIcon width={14} height={14} aria-label='소스코드 추가' />
          </Button>

          <TagInput {...tagProps} />

          <Flex justify='flex-end' padding='0.5rem 0 0 0' width='100%'>
            <Flex gap='0.5rem'>
              <Button size='medium' variant='outlined' onClick={handleCancelButton}>
                취소
              </Button>
              <Button
                size='medium'
                variant='contained'
                onClick={handleSaveButtonClick}
                disabled={sourceCodes.length === 0}
              >
                저장
              </Button>
            </Flex>
          </Flex>

          {error && <Text.Medium color={theme.color.light.analogous_primary_400}>Error: {error.message}</Text.Medium>}
        </Flex>
      </S.TemplateEditContainer>
      <S.SidebarContainer>
        <SelectList>
          {sourceCodes.map((sourceCode, index) => (
            <SelectList.Option key={index} onClick={handleSelectList(index)} isSelected={currentFile === index}>
              {sourceCode.filename}
            </SelectList.Option>
          ))}
        </SelectList>
      </S.SidebarContainer>
    </Flex>
  );
};

export default TemplateEdit;

interface NewCategoryInputProps {
  createNewCategory: (e: React.KeyboardEvent<HTMLInputElement>) => void;
}

const NewCategoryInput = ({ createNewCategory }: NewCategoryInputProps) => (
  <Input size='medium' variant='outlined' inputColor={theme.color.light.secondary_400}>
    <Input.TextField
      autoFocus
      placeholder='+ 새 카테고리 생성'
      onKeyUpCapture={createNewCategory}
      placeholderColor={theme.color.light.secondary_600}
    />
  </Input>
);

interface CategoryGuideProps {
  isOpen: boolean;
  isPending: boolean;
}

const CategoryGuide = ({ isOpen, isPending }: CategoryGuideProps) => (
  <Guide isOpen={isOpen} css={{ marginTop: '0.5rem', marginBottom: '-0.5rem' }}>
    {isPending ? (
      <>
        <Text.Medium color={theme.color.light.secondary_400}>카테고리 생성중!!</Text.Medium>
        <Text.Medium color={theme.color.light.secondary_400}>생성 후 자동 선택됩니다</Text.Medium>
      </>
    ) : (
      <>
        <Text.Small color={theme.color.light.secondary_400}>새 카테고리명을 입력하고 엔터를 눌러</Text.Small>
        <Text.Small color={theme.color.light.secondary_400}>쉽게 카테고리를 등록할 수 있어요!!</Text.Small>
      </>
    )}
  </Guide>
);
