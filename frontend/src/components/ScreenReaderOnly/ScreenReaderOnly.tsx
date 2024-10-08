const ScreenReaderOnly = () => (
  <div
    id='screen-reader'
    aria-live='polite'
    aria-hidden='true'
    style={{
      position: 'absolute',
      width: '1px',
      height: '1px',
      margin: '-1px',
      padding: '0',
      border: '0',
      overflow: 'hidden',
      clip: 'rect(0, 0, 0, 0)',
      whiteSpace: 'nowrap',
      wordWrap: 'normal',
    }}
  />
);

export default ScreenReaderOnly;
