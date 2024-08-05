import { trashcanIcon } from '@/assets/images';
import { useCategoryUpload } from '@/hooks/category/query/useCategoryUpload';
import { Category } from '@/types/category';
import { Snippet } from '@/types/template';
import { Button } from '../Button/style';
import Dropdown from '../Dropdown/Dropdown';
import Flex from '../Flex/Flex';
import Input from '../Input/Input';
import SnippetEditor from '../SnippetEditor/SnippetEditor';
import TagInput from '../TagInput/TagInput';
import * as S from './TemplateEdit.style';

interface Props {
  title: string;
  description: string;
  snippets: Snippet[];
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
  handleDeleteSnippet: (index: number) => void;
  handleSaveButtonClick: () => Promise<void>;
  error: Error | null;
}

const TemplateEdit = ({
  title,
  description,
  snippets,
  tagProps,
  categoryProps,
  handleTitleChange,
  handleDescriptionChange,
  handleAddButtonClick,
  handleCancelButton,
  handleCodeChange,
  handleFileNameChange,
  handleDeleteSnippet,
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
            <Input size='medium'>
              <Input.TextField
                autoFocus
                placeholder='+ 새 카테고리 생성'
                onKeyUpCapture={(e) => {
                  if (e.target instanceof HTMLInputElement && e.key === 'Enter') {
                    console.log(e.target.value);

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
          <Input size='medium' variant='text'>
            <Input.TextField placeholder='제목을 입력해주세요' value={title} onChange={handleTitleChange} />
          </Input>
        </div>

        <div css={{ borderBottom: '1px solid #788496', width: '100%' }}>
          <Input size='medium' variant='text'>
            <Input.TextField placeholder='설명을 입력해주세요' value={description} onChange={handleDescriptionChange} />
          </Input>
        </div>

        <TagInput {...tagProps} />
        {snippets.map((snippet, idx) => (
          <Flex key={idx} style={{ position: 'relative' }} width='100%'>
            <SnippetEditor
              key={idx}
              fileName={snippet.filename}
              content={snippet.content}
              onChangeContent={(newContent) => handleCodeChange(newContent, idx)}
              onChangeFileName={(newFileName) => handleFileNameChange(newFileName, idx)}
            />
            <S.DeleteButton
              size='small'
              variant='text'
              onClick={() => {
                handleDeleteSnippet(idx);
              }}
            >
              <img src={trashcanIcon} width={20} height={20} alt='Delete snippet' />
            </S.DeleteButton>
          </Flex>
        ))}

        <Flex justify='space-between' padding='3rem 0 0 0' width='100%'>
          <Button size='small' variant='outlined' onClick={handleAddButtonClick}>
            + Add Snippet
          </Button>
          <Flex gap='1.6rem'>
            <Button size='small' variant='outlined' onClick={handleCancelButton}>
              cancel
            </Button>
            <Button size='small' variant='contained' onClick={handleSaveButtonClick} disabled={snippets.length === 0}>
              Save
            </Button>
          </Flex>
        </Flex>

        {error && <div style={{ color: 'red' }}>Error: {error.message}</div>}
      </Flex>
    </Flex>
  );
};

export default TemplateEdit;
