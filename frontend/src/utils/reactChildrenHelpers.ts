import { Children, isValidElement, ReactNode } from 'react';

export const getChildOfType = (children: ReactNode, type: unknown) => {
  const childrenArray = Children.toArray(children);

  return childrenArray.find((child) => isValidElement(child) && child.type === type);
};

export const getChildrenWithoutTypes = (children: ReactNode, types: unknown[]) => {
  const childrenArray = Children.toArray(children);

  return childrenArray.filter((child) => !(isValidElement(child) && types.includes(child.type)));
};
