export interface Category {
  id: number;
  name: string;
}

export interface CategoryList {
  categories: Category[];
}

export interface CategoryRequest {
  name: string;
}
