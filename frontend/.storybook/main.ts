import type { StorybookConfig } from '@storybook/react-webpack5';
import { Configuration } from 'webpack';
import path from 'path';

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
    config.module = config.module || {};
    config.module.rules = config.module.rules || [];

    const imageRule = config.module.rules.find((rule) => rule?.['test']?.test('.svg'));

    if (imageRule) {
      imageRule['exclude'] = /\.svg$/;
    }

    config.module?.rules?.push(
      {
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
      },
      {
        test: /\.svg$/,
        use: ['@svgr/webpack'],
      },
    );

    config.resolve = {
      ...config.resolve,
      alias: {
        ...config.resolve?.alias,
        '@': path.resolve(__dirname, '../src'),
      },
    };

    return config;
  },

  framework: {
    name: '@storybook/react-webpack5',
    options: {},
  },
};
export default config;
