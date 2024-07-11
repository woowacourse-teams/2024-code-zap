import { Global } from "@emotion/react";

const GlobalStyles = () => {
  return (
    <Global
      styles={{
        "*": {
          margin: 0,
          padding: 0,
          border: 0,
          font: "inherit",
          fontSize: "100%",
          verticalAlign: "baseline",
          boxSizing: "border-box",
        },
        body: {
          lineHeight: 1,
          listStyle: "none",
        },
      }}
    />
  );
};

export default GlobalStyles;
