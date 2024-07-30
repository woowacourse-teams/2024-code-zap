import { getLanguageByFilename } from './getLanguageByFileName';

describe('getLanguageByFilename', () => {
  it('JavaScript 파일에 대해 올바른 언어를 반환해야 한다', () => {
    expect(getLanguageByFilename('app.js')).toBe('javascript');
    expect(getLanguageByFilename('component.jsx')).toBe('javascript');
  });

  it('TypeScript 파일에 대해 올바른 언어를 반환해야 한다', () => {
    expect(getLanguageByFilename('index.ts')).toBe('typescript');
    expect(getLanguageByFilename('component.tsx')).toBe('typescript');
  });

  it('Java 파일에 대해 올바른 언어를 반환해야 한다', () => {
    expect(getLanguageByFilename('Main.java')).toBe('java');
  });

  it('Python 파일에 대해 올바른 언어를 반환해야 한다', () => {
    expect(getLanguageByFilename('script.py')).toBe('python');
  });

  it('C++ 파일에 대해 올바른 언어를 반환해야 한다', () => {
    expect(getLanguageByFilename('main.cpp')).toBe('cpp');
    expect(getLanguageByFilename('header.hpp')).toBe('cpp');
  });

  it('HTML 파일에 대해 올바른 언어를 반환해야 한다', () => {
    expect(getLanguageByFilename('index.html')).toBe('html');
  });

  it('CSS 파일에 대해 올바른 언어를 반환해야 한다', () => {
    expect(getLanguageByFilename('styles.css')).toBe('css');
  });

  it('Markdown 파일에 대해 올바른 언어를 반환해야 한다', () => {
    expect(getLanguageByFilename('README.md')).toBe('markdown');
  });

  it('알 수 없는 확장자에 대해 "plaintext"를 반환해야 한다', () => {
    expect(getLanguageByFilename('unknown.xyz')).toBe('plaintext');
  });

  it('확장자가 없는 파일명을 처리할 수 있어야 한다', () => {
    expect(getLanguageByFilename('Dockerfile')).toBe('plaintext');
  });

  it('여러 개의 점이 있는 파일명을 처리할 수 있어야 한다', () => {
    expect(getLanguageByFilename('test.spec.js')).toBe('javascript');
  });
});
