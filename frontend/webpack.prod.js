const { merge } = require('webpack-merge');
const Dotenv = require('dotenv-webpack');
const CompressionPlugin = require('compression-webpack-plugin');
const common = require('./webpack.common.js');

module.exports = () => {
  return merge(common, {
    mode: 'production',
    devtool: 'hidden-source-map',
    plugins: [
      new Dotenv({
        path: './.env.production',
        systemvars: true,
        safe: true,
        ignoreStub: true,
      }),
      new CompressionPlugin({
        algorithm: 'brotliCompress',
        exclude: /\.(png|jpg|gif|webp|webm|map|ico)$/i,
      }),
    ],
  });
};
