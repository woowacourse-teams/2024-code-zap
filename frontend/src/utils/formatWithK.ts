export const formatWithK = (number: number) => {
  if (number < 1000) {
    return number.toString();
  }

  const formattedNumber = (Math.floor(number / 100) / 10).toFixed(1);

  return `${formattedNumber}k`;
};
