export const getByteSize = (str: string) => new TextEncoder().encode(str).length;
