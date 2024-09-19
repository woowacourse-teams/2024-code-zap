import { SourceCode, Text } from '@/components';
import { useToggle } from '@/hooks';
import { useToast } from '@/hooks/useToast';
import { theme } from '@/style/theme';
import { getLanguageByFilename } from '@/utils';

import { SourceCodeMode } from '../SourceCode/SourceCode';
import * as S from './SourceCodeViewer.style';

interface Props {
  mode?: Exclude<SourceCodeMode, 'edit'>;
  filename?: string;
  content: string;
  sourceCodeRef?: React.Ref<HTMLInputElement> | null;
}

const SourceCodeViewer = ({ mode = 'detailView', filename = '', content, sourceCodeRef }: Props) => {
  const [isSourceCodeOpen, toggleSourceCode] = useToggle(true);

  const { infoAlert } = useToast();

  const copyCode = (content: string) => () => {
    navigator.clipboard.writeText(content);
    infoAlert('코드가 복사되었습니다!');
  };

  return (
    <S.SourceCodeViewerContainer id={filename} ref={sourceCodeRef}>
      {mode === 'detailView' && (
        <S.FilenameContainer>
          <S.ToggleBox onClick={toggleSourceCode}>
            <S.SourceCodeToggleIcon isOpen={isSourceCodeOpen} aria-label='소스코드 펼침' />
            <S.NoScrollbarContainer>
              <Text.Small color='#fff' weight='bold'>
                {filename}
              </Text.Small>
            </S.NoScrollbarContainer>
          </S.ToggleBox>
          <S.CopyButton size='small' variant='text' onClick={copyCode(content)}>
            <Text.Small color={theme.color.light.primary_500} weight='bold'>
              {'복사'}
            </Text.Small>
          </S.CopyButton>
        </S.FilenameContainer>
      )}
      <S.SourceCodeWrapper isOpen={isSourceCodeOpen}>
        {isSourceCodeOpen && <SourceCode mode={mode} content={content} language={getLanguageByFilename(filename)} />}
      </S.SourceCodeWrapper>
    </S.SourceCodeViewerContainer>
  );
};

export default SourceCodeViewer;
