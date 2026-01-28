export enum OrderStatus {
  PENDING = 'PENDING',
  PLACED = 'PLACED',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

export interface Order {
  id: number;
  orderDate: string; // ISO string from LocalDateTime
  status: OrderStatus | string;
  totalAmount: number;
  discountAmount: number;
  netAmount: number;
  shippedDate?: string;
  deliveredDate?: string;
  couponCode: string;
  orderItems: OrderItem[];
  userName: string;
}

export interface OrderItem {
  productId: number;
  productName: string;
  quantity: number;
  priceAtPurchase: number;
  imageUrl?: string;
}
