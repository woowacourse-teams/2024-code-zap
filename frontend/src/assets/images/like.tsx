type State = 'like' | 'unlike' | 'unClickable';

interface Props {
  state: State;
  size: number;
}

const LikeIcon = ({ state, size }: Props) => {
  const fillColor = {
    like: '#FF5936',
    unlike: 'none',
    unClickable: '#393E46',
  }[state];

  const strokeColor = {
    like: '#FF5936',
    unlike: '#393E46',
    unClickable: '#393E46',
  }[state];

  return (
    <svg width={size} height={size} viewBox='0 0 24 24' fill='none' xmlns='http://www.w3.org/2000/svg'>
      <path
        d='M20.42 4.58045C19.9183 4.07702 19.3222 3.67758 18.6658 3.40503C18.0094 3.13248 17.3057 2.99219 16.595 2.99219C15.8842 2.99219 15.1805 3.13248 14.5241 3.40503C13.8678 3.67758 13.2716 4.07702 12.77 4.58045L12 5.36045L11.23 4.58045C10.7283 4.07702 10.1322 3.67758 9.47578 3.40503C8.81941 3.13248 8.11568 2.99219 7.40496 2.99219C6.69425 2.99219 5.99052 3.13248 5.33414 3.40503C4.67776 3.67758 4.08164 4.07702 3.57996 4.58045C1.45996 6.70045 1.32996 10.2804 3.99996 13.0004L12 21.0004L20 13.0004C22.67 10.2804 22.54 6.70045 20.42 4.58045Z'
        fill={fillColor}
        stroke={strokeColor}
        strokeWidth='2'
        strokeLinecap='round'
        strokeLinejoin='round'
      />
    </svg>
  );
};

export default LikeIcon;
