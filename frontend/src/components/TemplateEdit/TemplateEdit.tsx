import { ChangeEvent, Dispatch, KeyboardEvent, MutableRefObject, SetStateAction } from 'react';

import { PlusIcon, TrashcanIcon } from '@/assets/images';
import { Button, Dropdown, Flex, Input, SourceCodeEditor, TagInput, Text, Guide } from '@/components';
import { useInputWithValidate } from '@/hooks/utils';
import { useCategoryUploadMutation } from '@/queries/category';
import { validateCategoryName } from '@/service/validates';
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
    dropdownRef: MutableRefObject<HTMLDivElement | null>;
  };
  tagProps: {
    tags: string[];
    setTags: Dispatch<SetStateAction<string[]>>;
    value: string;
    handleValue: (e: ChangeEvent<HTMLInputElement>) => void;
    resetValue: () => void;
  };
  handleTitleChange: (e: ChangeEvent<HTMLInputElement>) => void;
  handleDescriptionChange: (e: ChangeEvent<HTMLInputElement>) => void;
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

  const {
    value: categoryInputValue,
    errorMessage: categoryErrorMessage,
    handleChange: handleCategoryChange,
  } = useInputWithValidate('', validateCategoryName);

  const getExistingCategory = (value: string) =>
    categoryProps.options.find((category) => categoryProps.getOptionLabel(category) === value);

  const createNewCategory = (e: KeyboardEvent<HTMLInputElement>) => {
    if (!(e.target instanceof HTMLInputElement) || e.key !== 'Enter') {
      return;
    }

    const inputValue = e.target.value;
    const existingCategory = getExistingCategory(inputValue);

    if (existingCategory) {
      categoryProps.handleCurrentValue(existingCategory);

      return;
    }

    if (categoryErrorMessage !== '') {
      return;
    }

    const newCategory = { name: inputValue };

    postCategory(newCategory);

    e.target.value = '';
  };

  return (
    <S.TemplateEditContainer>
      <Flex direction='column' justify='center' align='flex-start' gap='1rem' width='100%'>
        <CategoryGuide
          isOpen={categoryProps.isOpen}
          isPending={isPending}
          categoryErrorMessage={categoryErrorMessage}
        />
        <Dropdown
          {...categoryProps}
          replaceChildrenWhenIsOpen={
            <NewCategoryInput
              categoryInputValue={categoryInputValue}
              createNewCategory={createNewCategory}
              handleChange={handleCategoryChange}
            />
          }
        />

        <S.UnderlineInputWrapper>
          <Input size='xlarge' variant='text'>
            <Input.TextField placeholder='제목을 입력해주세요' value={title} onChange={handleTitleChange} />
          </Input>
        </S.UnderlineInputWrapper>

        <Input size='large' variant='text'>
          <Input.TextField placeholder='설명을 입력해주세요' value={description} onChange={handleDescriptionChange} />
        </Input>

        {sourceCodes.map((sourceCode, idx) => (
          <Flex key={idx} style={{ position: 'relative' }} width='100%'>
            <SourceCodeEditor
              key={idx}
              index={idx}
              fileName={sourceCode.filename}
              content={sourceCode.content}
              onChangeContent={(newContent) => handleCodeChange(newContent, idx)}
              onChangeFileName={(newFileName) => handleFileNameChange(newFileName, idx)}
            />
            <S.DeleteButton
              size='small'
              variant='text'
              onClick={() => {
                handleDeleteSourceCode(idx);
              }}
            >
              <TrashcanIcon width={24} height={24} aria-label='템플릿 삭제' />
            </S.DeleteButton>
          </Flex>
        ))}
        <Button size='medium' variant='outlined' fullWidth onClick={handleAddButtonClick}>
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
  );
};

export default TemplateEdit;

interface NewCategoryInputProps {
  categoryInputValue: string;
  createNewCategory: (e: KeyboardEvent<HTMLInputElement>) => void;
  handleChange: (e: ChangeEvent<HTMLInputElement>, compareValue?: string) => void;
}

const NewCategoryInput = ({ categoryInputValue, createNewCategory, handleChange }: NewCategoryInputProps) => (
  <Input size='medium' variant='outlined' inputColor={theme.color.light.secondary_400}>
    <Input.TextField
      autoFocus
      placeholder='+ 새 카테고리 생성'
      value={categoryInputValue}
      onChange={handleChange}
      onKeyUpCapture={createNewCategory}
      placeholderColor={theme.color.light.secondary_600}
    />
  </Input>
);

interface CategoryGuideProps {
  isOpen: boolean;
  isPending: boolean;
  categoryErrorMessage: string;
}

const CategoryGuide = ({ isOpen, isPending, categoryErrorMessage }: CategoryGuideProps) => {
  const isError = categoryErrorMessage !== '';

  return (
    <Guide isOpen={isOpen} css={{ marginTop: '0.5rem', marginBottom: '-0.5rem' }}>
      {isPending ? (
        <>
          <Text.Medium color={theme.color.light.secondary_400}>카테고리를 생성 중이에요!</Text.Medium>
        </>
      ) : isError ? (
        <>
          <Text.Small color={theme.color.light.analogous_primary_300}>{categoryErrorMessage}</Text.Small>
        </>
      ) : (
        <>
          <Text.Small color={theme.color.light.secondary_400}>엔터로 카테고리를 등록해요</Text.Small>
        </>
      )}
    </Guide>
  );
};
