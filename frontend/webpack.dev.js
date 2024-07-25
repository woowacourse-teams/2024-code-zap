const { HotModuleReplacementPlugin } = require('webpack');
const { merge } = require('webpack-merge');
const Dotenv = require('dotenv-webpack');

const common = require('./webpack.common.js');

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
