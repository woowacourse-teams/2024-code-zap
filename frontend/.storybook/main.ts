import type { StorybookConfig } from '@storybook/react-webpack5';
import { Configuration } from 'webpack';

const config: StorybookConfig = {
  stories: ['../src/**/*.mdx', '../src/**/*.stories.@(js|jsx|mjs|ts|tsx)'],
  addons: [
    '@storybook/addon-webpack5-compiler-swc',
    '@storybook/addon-onboarding',
    '@storybook/addon-links',
    '@storybook/addon-essentials',
    '@chromatic-com/storybook',
    '@storybook/addon-interactions',
  ],
  webpackFinal: async (config: Configuration) => {
    config.module?.rules?.push({
      test: /\.(ts|tsx)$/,
      use: [
        {
          loader: require.resolve('ts-loader'),
          options: {
            transpileOnly: true,
            compilerOptions: {
              jsx: 'react-jsx',
              jsxImportSource: '@emotion/react',
            },
          },
        },
      ],
    });

    return config;
  },

  framework: {
    name: '@storybook/react-webpack5',
    options: {},
  },
};
export default config;
