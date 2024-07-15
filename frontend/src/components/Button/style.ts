import { css } from '@emotion/react';

export const style = css({
  cursor: 'pointer',
  borderRadius: '8px',
  textAlign: 'center',
  padding: '0.8rem 1.6rem',

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
    opacity: 0.6,
    cursor: 'default',
  },
});

export const types = {
  default: css({
    background: 'rgba(255, 211, 105, 1)',
    color: 'rgba(52, 60, 72, 1)',
  }),
  outlined: css({
    background: 'none',
    border: '0.1rem solid rgba(255, 211, 105, 1)',
    color: 'rgba(255, 211, 105, 1)',
  }),
  text: css({
    background: 'none',
    color: 'rgba(255, 211, 105, 1)',
  }),
};

export const sizes = {
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
  minWidth: '0',
  display: 'inline-block',
});
