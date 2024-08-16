import { newTemplateIcon, trashcanIcon } from '@/assets/images';
import { Button, Dropdown, Flex, Input, SourceCodeEditor, TagInput } from '@/components';
import { useCategoryUpload } from '@/queries/category';
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
  const { mutateAsync: postCategory } = useCategoryUpload();

  return (
    <Flex
      direction='column'
      justify='center'
      align='flex-start'
      gap='1.5rem'
      margin='1rem 0 0 0'
      css={{ maxWidth: '53rem', margin: 'auto', marginTop: '3rem' }}
    >
      <Flex direction='column' justify='center' align='flex-start' gap='1rem' width='100%'>
        <Dropdown
          {...categoryProps}
          replaceChildrenWhenIsOpen={
            <Input size='medium' variant='outlined'>
              <Input.TextField
                autoFocus
                placeholder='+ 새 카테고리 생성'
                onKeyUpCapture={(e) => {
                  if (e.target instanceof HTMLInputElement && e.key === 'Enter') {
                    const newCategory = { name: e.target.value };

                    postCategory(newCategory);
                    e.target.value = '';
                  }
                }}
              />
            </Input>
          }
        />

        <div css={{ borderBottom: '1px solid #788496', width: '100%' }}>
          <Input size='xlarge' variant='text'>
            <Input.TextField placeholder='제목을 입력해주세요' value={title} onChange={handleTitleChange} />
          </Input>
        </div>

        <div css={{ borderBottom: '1px solid #788496', width: '100%' }}>
          <Input size='medium' variant='text'>
            <Input.TextField placeholder='설명을 입력해주세요' value={description} onChange={handleDescriptionChange} />
          </Input>
        </div>

        {sourceCodes.map((sourceCode, idx) => (
          <Flex key={idx} style={{ position: 'relative' }} width='100%'>
            <SourceCodeEditor
              key={idx}
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
              <img src={trashcanIcon} width={20} height={20} alt='Delete sourceCode' />
            </S.DeleteButton>
          </Flex>
        ))}
        <Button size='medium' variant='outlined' fullWidth onClick={handleAddButtonClick}>
          <img src={newTemplateIcon} width={14} height={14} />
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

        {error && <div style={{ color: 'red' }}>Error: {error.message}</div>}
      </Flex>
    </Flex>
  );
};

export default TemplateEdit;
