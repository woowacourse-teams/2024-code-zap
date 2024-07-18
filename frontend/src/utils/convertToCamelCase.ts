type Primitive = string | number | boolean | null | undefined;
type DeepObject = {
  [key: string]: DeepObject | Primitive | Array<DeepObject | Primitive>;
};

const toCamelCase = (str: string): string =>
  str.replace(/([-_][a-z])/g, (group) => group.toUpperCase().replace('-', '').replace('_', ''));

export const convertToCamelCase = <T extends DeepObject | Primitive | Array<DeepObject | Primitive>>(obj: T): T => {
  if (Array.isArray(obj)) {
    return obj.map(convertToCamelCase) as T;
  } else if (obj !== null && typeof obj === 'object') {
    const result: { [key: string]: unknown } = {};

    for (const key in obj) {
      if (Object.prototype.hasOwnProperty.call(obj, key)) {
        const camelKey = toCamelCase(key);

        result[camelKey] = convertToCamelCase(obj[key] as DeepObject | Primitive | Array<DeepObject | Primitive>);
      }
    }

    return result as T;
  }

  return obj;
};
