import { useNavigate, useLocation, NavigateOptions, To } from 'react-router-dom';

export const useCustomNavigate = () => {
  const navigate = useNavigate();
  const location = useLocation();

  return (to: To | number, options?: NavigateOptions) => {
    if (typeof to === 'number') {
      navigate(to);

      return;
    }

    if (typeof to === 'string' && to !== location.pathname) {
      navigate(to, options);

      return;
    }

    if (typeof to === 'object' && to.pathname !== location.pathname) {
      navigate(to, options);
    }
  };
};
