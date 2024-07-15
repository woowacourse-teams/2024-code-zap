import { css } from '@emotion/react';

export const style = css({
  padding: '14px',
  borderRadius: '9px',
  border: '1px solid #808080',
  background: '#eeeeee',
  width: '450px',
  height: '40px',

  '&::placeholder': {
    color: '#808080',
  },
  '&:focus': {
    borderColor: 'black',
  },
  '&:disabled': {
    backgroundColor: '#f5f5f5',
    borderColor: '#ddd',
    cursor: 'default',
    opacity: 0.6,
  },
});

export const searchStyle = {
  paddingLeft: '42px',
};

export const inputWrapperStyle = css({
  position: 'relative',
  display: 'inline-block',
  width: '450px',
});

export const iconStyle = css({
  position: 'absolute',
  left: '14px',
  top: '50%',
  transform: 'translateY(-50%)',
  width: '20px',
  height: '20px',
});
