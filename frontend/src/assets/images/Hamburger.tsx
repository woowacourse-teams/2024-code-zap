interface Props {
  menuOpen?: boolean;
  onClick?: () => void;
}

const HamburgerIcon = ({ menuOpen, onClick }: Props) => {
  console.log('menuOpen', menuOpen);

  return (
    <svg
      width='24'
      height='24'
      viewBox='0 0 24 24'
      fill='none'
      xmlns='http://www.w3.org/2000/svg'
      onClick={onClick}
      style={{ cursor: 'pointer' }}
    >
      <path d='M5 17H19M5 12H19M5 7H19' stroke='black' strokeWidth='2' strokeLinecap='round' strokeLinejoin='round' />
    </svg>
  );
};

export default HamburgerIcon;
