import { TemplateEdit } from './components';
import { useTemplateUpload } from './hooks';

const TemplateUploadPage = () => {
  const props = useTemplateUpload();

  return <TemplateEdit {...props} />;
};

export default TemplateUploadPage;
