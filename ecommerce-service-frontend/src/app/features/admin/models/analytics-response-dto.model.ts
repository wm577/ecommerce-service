export interface AnalyticsResponseDto {
  currentMonthOrders: number;
  currentMonthEarnings: number;
  previousMonthOrders: number;
  previousMonthEarnings: number;
  orderGrowth: number;    // Percentage
  earningGrowth: number;  // Percentage
  averageOrderValue: number;
  placed: number;
  shipped: number;
  delivered: number;
}
