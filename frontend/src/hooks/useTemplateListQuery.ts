import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { TemplateListResponse } from '@/types/template';

const fetchTemplateList = async (): Promise<TemplateListResponse> => {
  const apiUrl = process.env.REACT_APP_API_URL;
  const response = await fetch(`${apiUrl}/templates`);
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  return response.json();
};

const useTemplateListQuery = (): UseQueryResult<TemplateListResponse, Error> => {
  return useQuery<TemplateListResponse, Error>({
    queryKey: ['templateList'],
    queryFn: fetchTemplateList,
  });
};

export default useTemplateListQuery;
