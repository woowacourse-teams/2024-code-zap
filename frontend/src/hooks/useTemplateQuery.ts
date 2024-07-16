import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { Template } from '@/types/template';

const fetchTemplate = async (id: string): Promise<Template> => {
  const response = await fetch(`http://localhost:8080/templates/${id}`);
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
};

const useTemplateQuery = (id: string): UseQueryResult<Template, Error> => {
  return useQuery<Template, Error>({
    queryKey: ['template', id],
    queryFn: () => fetchTemplate(id),
  });
};

export default useTemplateQuery;
