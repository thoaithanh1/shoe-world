import { Color } from "./color";
import { Size } from "./size";

export interface ProductDetail {
    id?: number;
    price: number;
    gender: number;
    status: boolean;
    color: Color;
    size: Size;
}