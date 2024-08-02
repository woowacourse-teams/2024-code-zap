const { merge } = require('webpack-merge');
const Dotenv = require('dotenv-webpack');

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
    ],
  });
};
