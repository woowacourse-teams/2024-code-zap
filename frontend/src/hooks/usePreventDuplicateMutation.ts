import { useMutation, UseMutationOptions, UseMutationResult, MutationFunction } from '@tanstack/react-query';
import { useRef } from 'react';

type RequiredMutationFn<TData = unknown, TError = Error, TVariables = void, TContext = unknown> = UseMutationOptions<
  TData,
  TError,
  TVariables,
  TContext
> & {
  mutationFn: MutationFunction<TData, TVariables>;
};

export const usePreventDuplicateMutation = <TData = unknown, TError = Error, TVariables = void, TContext = unknown>(
  options: RequiredMutationFn<TData, TError, TVariables, TContext>,
): UseMutationResult<TData, TError, TVariables, TContext> => {
  const isMutatingRef = useRef(false);

  const originalMutationFn = options.mutationFn;

  const preventDuplicateMutationFn: MutationFunction<TData, TVariables> = async (variables: TVariables) => {
    if (isMutatingRef.current) {
      return undefined as TData;
    }

    isMutatingRef.current = true;

    try {
      return await originalMutationFn(variables);
    } finally {
      isMutatingRef.current = false;
    }
  };

  return useMutation({
    ...options,
    mutationFn: preventDuplicateMutationFn,
  });
};
