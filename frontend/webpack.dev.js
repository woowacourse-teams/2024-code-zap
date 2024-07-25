const webpack = require('webpack');
const { merge } = require('webpack-merge');
const { HotModuleReplacementPlugin } = require('webpack');
const common = require('./webpack.config.js');
const Dotenv = require('dotenv-webpack');

module.exports = () => {
  return merge(common, {
    mode: 'development',
    devtool: 'cheap-module-source-map',
    plugins: [
      new HotModuleReplacementPlugin(),
      new Dotenv({
        path: './.env.development',
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
