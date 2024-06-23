import { Brand } from "./brand";
import { Category } from "./category";

export interface Product {
    id?: number;
    name: string;
    material: string;
    model: string;
    brand: Brand;
    category: Category;
    description: string;
}