type DeepObject = {
  [key: string]: DeepObject | any;
};

const toCamelCase = (str: string): string => {
  return str.replace(/([-_][a-z])/g, (group) =>
    group.toUpperCase().replace('-', '').replace('_', '')
  );
};

export const convertToCamelCase = <T extends DeepObject | any[]>(
  obj: T
): any => {
  if (Array.isArray(obj)) {
    return obj.map(convertToCamelCase);
  } else if (obj !== null && typeof obj === 'object') {
    return Object.keys(obj).reduce(
      (result, key) => {
        const camelKey = toCamelCase(key);
        result[camelKey] = convertToCamelCase(obj[key]);
        return result;
      },
      {} as { [key: string]: any }
    );
  }
  return obj;
};
