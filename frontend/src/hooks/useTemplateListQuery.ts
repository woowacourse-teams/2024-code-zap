import { useQuery } from '@tanstack/react-query';
import type { TemplateListResponse } from '@/types/template';

const fetchTemplateList = async (): Promise<TemplateListResponse> => {
  // change this url after MSW initial setting
  // const apiUrl = process.env.REACT_APP_API_URL;
  const response = await fetch('http://localhost:8080/templates', {
    method: 'GET',
  });

  if (!response.ok) {
    throw new Error('Network response was not ok');
  }

  return response.json();
};

export const useTemplateListQuery = () => {
  const { data, error, isLoading } = useQuery<TemplateListResponse, Error>({
    queryKey: ['templateList'],
    queryFn: fetchTemplateList,
  });

  return { data, error, isLoading };
};
