declare module '*.png' {
  const value: string;
  export default value;
}

declare module '*.svg' {
  import React = require('react');

  const ReactComponent: React.FunctionComponent<React.SVGProps<SVGSVGElement>>;
  export default ReactComponent;
}
