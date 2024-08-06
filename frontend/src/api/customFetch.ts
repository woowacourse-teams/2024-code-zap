interface Props {
  url: string;
  method?: 'GET' | 'POST' | 'PATCH' | 'DELETE';
  headers?: RequestInit['headers'];
  body?: RequestInit['body'];
  errorMessage?: string;
}

export const customFetch = async ({ url, headers, method = 'GET', body }: Props) => {
  try {
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json',
        ...headers,
      },
      credentials: 'include',
      body,
    });

    if (method !== 'GET') {
      return response;
    }

    const data = await response.json();

    return data;
  } catch (error) {
    throw new Error(String(error));
  }
};
