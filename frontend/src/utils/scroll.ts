const top = (behavior?: 'smooth' | 'instant' | 'auto') => {
  window.scrollTo({
    top: 0,
    behavior: behavior ? behavior : 'auto',
  });
};

export const scroll = {
  top,
};
