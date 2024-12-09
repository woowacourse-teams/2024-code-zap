export const isFilenameEmpty = (filename: string) => !filename.trim();

export const generateUniqueFilename = () => `file_${Math.random().toString(36).substring(2, 10)}.txt`;
