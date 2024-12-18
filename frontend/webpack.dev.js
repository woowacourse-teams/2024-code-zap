const { HotModuleReplacementPlugin } = require('webpack');
const { merge } = require('webpack-merge');
const Dotenv = require('dotenv-webpack');

const common = require('./webpack.common.js');

const apiEnv = process.env.APP_ENV ?? 'development';
const envFilePath = `.env.${apiEnv}`;

module.exports = () => {
  return merge(common, {
    mode: 'development',
    devtool: 'cheap-module-source-map',
    output: {
      publicPath: '/',
    },
    plugins: [
      new HotModuleReplacementPlugin(),
      new Dotenv({
        path: `./${envFilePath}`,
      }),
    ],
    module: {
      rules: [
        {
          test: /\.(jpe?g|png|gif)$/i,
          type: 'asset/inline',
        },
      ],
    },
  });
};
