const top = (behavior?: 'smooth' | 'instant' | 'auto') => {
  window.scrollTo({
    top: 0,
    behavior: behavior ? (behavior as ScrollBehavior) : 'auto',
  });
};

export const scroll = {
  top,
};
