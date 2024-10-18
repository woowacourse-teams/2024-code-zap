export const getLanguageByFilename = (filename: string) => {
  const extension = getFileExtension(filename);
  const language = getLanguageByExtension(extension);

  return language;
};

export const getLanguageForAutoTag = (filename: string) => {
  const extension = getFileExtension(filename);
  const language = getLanguageByExtension(extension);

  if (extension === 'jsx' || extension === 'tsx') {
    return 'react';
  }

  if (language === 'plaintext') {
    return '';
  }

  return language;
};

const getFileExtension = (filename: string) => {
  if (filename.includes('.')) {
    const parts = filename.split('.');

    return parts.pop() || '';
  }

  return '';
};

const getLanguageByExtension = (extension: string) => extensionToLanguage[extension] || 'plaintext';

const extensionToLanguage: { [key: string]: string } = {
  '1c': '1c',
  adoc: 'asciidoc',
  ado: 'stata',
  adb: 'ada',
  ads: 'ada',
  applescript: 'applescript',
  as: 'actionscript',
  asc: 'asciidoc',
  asm: 'x86asm',
  asp: 'asp',
  awk: 'awk',
  bash: 'bash',
  bas: 'basic',
  bat: 'dos',
  bf: 'brainfuck',
  bmx: 'blitzmax',
  boo: 'boo',
  c: 'c',
  cbl: 'cobol',
  cc: 'cpp',
  clj: 'clojure',
  cls: 'cos',
  cmake: 'cmake',
  coffee: 'coffeescript',
  cpp: 'cpp',
  hpp: 'cpp',
  cr: 'crystal',
  cs: 'csharp',
  css: 'css',
  cxx: 'cpp',
  d: 'd',
  dart: 'dart',
  dcl: 'clean',
  dfm: 'delphi',
  dpr: 'delphi',
  druby: 'ruby',
  dsconfig: 'dsconfig',
  dts: 'dts',
  ecl: 'ecl',
  elm: 'elm',
  erl: 'erlang',
  ex: 'elixir',
  exs: 'elixir',
  f90: 'fortran',
  for: 'fortran',
  frag: 'glsl',
  fs: 'fsharp',
  gemspec: 'ruby',
  glsl: 'glsl',
  go: 'go',
  gql: 'graphql',
  gradle: 'gradle',
  graphql: 'graphql',
  groovy: 'groovy',
  haml: 'haml',
  hbs: 'handlebars',
  hcl: 'hcl',
  hlsl: 'hlsl',
  hs: 'haskell',
  html: 'html',
  hx: 'haxe',
  hxx: 'cpp',
  iced: 'iced',
  idl: 'idl',
  ini: 'ini',
  ino: 'arduino',
  i7x: 'inform7',
  ipynb: 'python',
  java: 'java',
  jl: 'julia',
  js: 'javascript',
  jsx: 'javascript',
  kt: 'kotlin',
  less: 'less',
  lhs: 'haskell',
  lisp: 'lisp',
  lock: 'yaml',
  lua: 'lua',
  m: 'objectivec',
  mak: 'makefile',
  markdown: 'markdown',
  md: 'markdown',
  mk: 'makefile',
  ml: 'ocaml',
  mli: 'ocaml',
  ms: 'javascript',
  nix: 'nix',
  nsi: 'nsis',
  nsh: 'nsis',
  nu: 'nu',
  php: 'php',
  pl: 'perl',
  pm: 'perl',
  pyd: 'python',
  py: 'python',
  r: 'r',
  rake: 'ruby',
  rb: 'ruby',
  rhtml: 'erb',
  rs: 'rust',
  rss: 'xml',
  sass: 'sass',
  scala: 'scala',
  scss: 'scss',
  sh: 'shell',
  sls: 'yaml',
  spec: 'ruby',
  sql: 'sql',
  styl: 'stylus',
  swift: 'swift',
  tex: 'latex',
  thor: 'ruby',
  toml: 'toml',
  ts: 'typescript',
  tsx: 'typescript',
  txt: 'plaintext',
  vala: 'vala',
  vb: 'vbnet',
  vbs: 'vbscript',
  vh: 'verilog',
  vhdl: 'vhdl',
  vim: 'vim',
  vue: 'vue',
  xhtml: 'xml',
  xml: 'xml',
  xquery: 'xquery',
  yml: 'yaml',
  zep: 'zephir',
};
