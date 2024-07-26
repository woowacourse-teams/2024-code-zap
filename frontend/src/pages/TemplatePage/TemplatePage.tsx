import { useRef, useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

import { pencilIcon, trashcanIcon } from '@/assets/images';
import { Flex, SelectList, Text } from '@/components';
import { useTemplateDeleteQuery, useTemplateQuery } from '@/hooks/template';
import { TemplateEditPage } from '@/pages';
import { formatRelativeTime } from '@/utils';
import * as S from './TemplatePage.style';

const TemplatePage = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const { data: template, error, isLoading } = useTemplateQuery(Number(id));
  const { mutateAsync: deleteTemplate } = useTemplateDeleteQuery(Number(id));

  const [currentFile, setCurrentFile] = useState<number | null>(null);
  const [isEdit, setIsEdit] = useState(false);

  const snippetRefs = useRef<(HTMLDivElement | null)[]>([]);

  useEffect(() => {
    if (template && template.snippets.length > 0) {
      setCurrentFile(template.snippets[0].id as number);
    }
  }, [template]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error.message}</div>;
  }

  if (!template) {
    return <div>No data available</div>;
  }

  const toggleEditButton = () => {
    setIsEdit((prev) => !prev);
  };

  const handleEditButtonClick = () => {
    toggleEditButton();
  };

  const handleSelectOption = (index: number) => (event: React.MouseEvent<HTMLAnchorElement>) => {
    event.preventDefault();

    const targetElement = snippetRefs.current[index];
    const headerHeight = 68;

    if (targetElement) {
      const elementPosition = targetElement.getBoundingClientRect().top;
      const offsetPosition = elementPosition + window.scrollY - headerHeight;

      window.scrollTo({
        top: offsetPosition,
        behavior: 'smooth',
      });
    }

    const id = template.snippets[index].id;

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

  return (
    <>
      {isEdit ? (
        <TemplateEditPage template={template} toggleEditButton={toggleEditButton} />
      ) : (
        <Flex direction='column' align='center' padding='10rem 0 0 0' width='100%'>
          <S.MainContainer>
            <Flex justify='space-between'>
              <Flex direction='column' gap='1.6rem'>
                <Text.Title color='white'>{template.title}</Text.Title>
                <Text.Caption color='#ffd369' weight='bold'>
                  {formatRelativeTime(template.modifiedAt)}
                </Text.Caption>
              </Flex>
              <Flex align='center' gap='1.6rem'>
                <S.EditButton
                  size='small'
                  variant='text'
                  onClick={() => {
                    handleEditButtonClick();
                  }}
                >
                  <img src={pencilIcon} width={24} height={24} alt='Delete snippet' />
                </S.EditButton>
                <S.DeleteButton
                  size='small'
                  variant='text'
                  onClick={() => {
                    handleDelete();
                  }}
                >
                  <img src={trashcanIcon} width={28} height={28} alt='Delete snippet' />
                </S.DeleteButton>
              </Flex>
            </Flex>

            {template.snippets.map((snippet, index) => (
              <div id={snippet.filename} key={snippet.id} ref={(el) => (snippetRefs.current[index] = el)}>
                <Flex
                  align='center'
                  height='3rem'
                  padding='1rem 1.5rem'
                  style={{ background: '#393e46', borderRadius: '8px 8px 0 0' }}
                >
                  <Text.Caption color='#fff' weight='bold'>
                    {snippet.filename}
                  </Text.Caption>
                </Flex>
                <SyntaxHighlighter
                  language='javascript'
                  style={vscDarkPlus}
                  showLineNumbers={true}
                  customStyle={{
                    borderRadius: '0 0 8px 8px',
                    width: '100%',
                    tabSize: 2,
                    margin: 0,
                  }}
                  codeTagProps={{
                    style: {
                      fontSize: '2.4rem',
                      lineHeight: '1.2rem',
                    },
                  }}
                >
                  {snippet.content}
                </SyntaxHighlighter>
              </div>
            ))}
          </S.MainContainer>

          <S.SidebarContainer>
            <SelectList>
              {template.snippets.map((snippet, index) => (
                <SelectList.Option
                  key={snippet.id}
                  onClick={handleSelectOption(index)}
                  isSelected={currentFile === snippet.id}
                >
                  {snippet.filename}
                </SelectList.Option>
              ))}
            </SelectList>
          </S.SidebarContainer>
        </Flex>
      )}
    </>
  );
};

export default TemplatePage;
