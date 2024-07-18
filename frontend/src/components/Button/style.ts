import { css } from '@emotion/react';

export const buttonStyle = css({
  cursor: 'pointer',
  borderRadius: '8px',
  textAlign: 'center',
  padding: '0.8rem 1.6rem',
  display: 'flex',
  alignItems: 'center',
  gap: '0.8rem',

  '&:not(:disabled):focus': {
    outline: 'none',
  },

  '&:not(:disabled):hover': {
    opacity: 0.8,
  },

  '&:not(:disabled):active': {
    opacity: 0.6,
  },

  '&:disabled': {
    cursor: 'default',
    opacity: 0.6,
  },
});

export const stylesByType = {
  default: css({
    background: 'rgb(255 211 105 / 100%)',
    color: 'rgb(52 60 72 / 100%)',
  }),
  outlined: css({
    background: 'none',
    border: '0.1rem solid rgb(255 211 105 / 100%)',
    color: 'rgb(255 211 105 / 100%)',
  }),
  text: css({
    background: 'none',
    color: 'rgb(255 211 105 / 100%)',
  }),
};

export const stylesBySize = {
  small: css({
    height: '3rem',
    fontWeight: 700,
    fontSize: '1.4rem',
  }),
  medium: css({
    height: '4rem',
    fontWeight: 700,
    fontSize: '1.8rem',
  }),
};

export const textTypeStyle = css({
  padding: '0',
  background: 'none',
  border: 'none',
  width: 'fit-content',
  height: 'fit-content',
  display: 'inline-block',
});
