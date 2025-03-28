import type { Config } from 'jest';

const config: Config = {
  verbose: true,
  preset: 'ts-jest',
  testEnvironment: 'jest-fixed-jsdom',
  setupFiles: ['./jest.polyfills.js', './jest.i18n.ts'],
  testEnvironmentOptions: {
    customExportConditions: [''],
  },
  setupFilesAfterEnv: ['./setupTests.ts'],
  moduleFileExtensions: ['ts', 'tsx', 'js', 'jsx', 'json'],
  transform: {
    '^.+\\.tsx?$': 'ts-jest',
    '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$':
      '<rootDir>/src/mocks/settings/fileTransformer.js',
  },
  clearMocks: true,
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1',
    '^@design/(.*)$': '<rootDir>/../design-system/$1',
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy',
    'react-syntax-highlighter/dist/esm': 'react-syntax-highlighter/dist/cjs',
  },
  transformIgnorePatterns: ['/node_modules/(?!react-syntax-highlighter)'],
  testPathIgnorePatterns: ['/playwright/'],
};

export default config;
