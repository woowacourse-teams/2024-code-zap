import { UseQueryResult, useQuery } from '@tanstack/react-query';
import { Template } from '@/types/template';

const fetchTemplate = async (id: string): Promise<Template> => {
  const apiUrl = process.env.REACT_APP_API_URL;
  const response = await fetch(`${apiUrl}/templates/${id}`);

  if (!response.ok) {
    throw new Error('Network response was not ok');
  }

  return response.json();
};

export const useTemplateQuery = (id: string): UseQueryResult<Template, Error> =>
  useQuery<Template, Error>({
    queryKey: ['template', id],
    queryFn: () => fetchTemplate(id),
  });
