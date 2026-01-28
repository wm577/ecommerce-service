export interface CartItemDto {
  id: number;
  productId: number;
  productName: string;
  price: number;
  quantity: number;
  byteImg?: string;      // Base64 string for the thumbnail
  totalAmount: number;   // Calculated as (price * quantity)
}

export interface CartResponseDto {
  id: number;
  userId: number;
  totalAmount: number;   // The subtotal of the entire cart
  cartItems: CartItemDto[];
}
