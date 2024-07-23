interface Props {
  url: string;
  method?: 'GET' | 'POST' | 'PATCH' | 'DELETE';
  headers?: RequestInit['headers'];
  body?: RequestInit['body'];
  errorMessage?: string;
}

export const customFetch = async ({
  url,
  headers,
  method = 'GET',
  body,
  errorMessage = '[Error] response was not ok',
}: Props) => {
  try {
    const response = await fetch(url, {
      method,
      headers: {
        ...headers,
      },
      body,
    });

    if (!response.ok) {
      throw new Error(errorMessage);
    }

    if (method !== 'GET') {
      return response;
    }

    const data = await response.json();

    return data;
  } catch (error) {
    throw new Error(errorMessage);
  }
};
