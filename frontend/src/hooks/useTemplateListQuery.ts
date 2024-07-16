import { useQuery, UseQueryResult } from '@tanstack/react-query';
import { TemplateListResponse } from '@/types/template';

const fetchTemplateList = async (): Promise<TemplateListResponse> => {
  const response = await fetch('http://localhost:8080/templates');
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