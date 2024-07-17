import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query';
import { CreateTemplateRequest, Template } from '@/types/template';

const createTemplate = async (newTemplate: CreateTemplateRequest): Promise<Template> => {
  const apiUrl = process.env.REACT_APP_API_URL;
  const response = await fetch(`${apiUrl}/templates`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(newTemplate),
  });

  if (!response.ok) {
    throw new Error('Failed to create template');
  }

  if (response.status === 201) {
    return newTemplate as Template;
  }

  return response.json();
};

const useTemplateUploadQuery = (): UseMutationResult<Template, Error, CreateTemplateRequest> => {
  const queryClient = useQueryClient();

  return useMutation<Template, Error, CreateTemplateRequest>({
    mutationFn: createTemplate,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['templateList'] });
    },
  });
};

export default useTemplateUploadQuery;
