import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Stock {
    private String symbol;
    private String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}

class Portfolio {
    private double cashBalance;
    private Map<String, Integer> holdings; // Symbol -> Quantity

    public Portfolio(double initialBalance) {
        this.cashBalance = initialBalance;
        this.holdings = new HashMap<>();
    }

    public double getCashBalance() { return cashBalance; }

    public void buyStock(Stock stock, int quantity) {
        double totalCost = stock.getPrice() * quantity;
        if (totalCost > cashBalance) {
            System.out.println("❌ Insufficient funds to complete transaction.");
            return;
        }
        cashBalance -= totalCost;
        holdings.put(stock.getSymbol(), holdings.getOrDefault(stock.getSymbol(), 0) + quantity);
        System.out.printf("✅ Successfully bought %d shares of %s.\n", quantity, stock.getSymbol());
    }

    public void sellStock(Stock stock, int quantity) {
        String symbol = stock.getSymbol();
        int currentQuantity = holdings.getOrDefault(symbol, 0);

        if (currentQuantity < quantity) {
            System.out.println("❌ You do not own enough shares to sell.");
            return;
        }

        double totalRevenue = stock.getPrice() * quantity;
        cashBalance += totalRevenue;
        if (currentQuantity == quantity) {
            holdings.remove(symbol);
        } else {
            holdings.put(symbol, currentQuantity - quantity);
        }
        System.out.printf("✅ Successfully sold %d shares of %s.\n", quantity, symbol);
    }

    public void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\n================ PORTFOLIO SUMMARY ================");
        System.out.printf("Available Cash: $%.2f\n", cashBalance);
        System.out.println("--------------------------------------------------");
        System.out.printf("%-10s %-10s %-15s %-15s\n", "Symbol", "Shares", "Current Price", "Total Value");

        double totalPortfolioValue = cashBalance;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int shares = entry.getValue();
            Stock stock = market.get(symbol);
            double value = shares * stock.getPrice();
            totalPortfolioValue += value;
            System.out.printf("%-10s %-10d $%-14.2f $%-14.2f\n", symbol, shares, stock.getPrice(), value);
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Total Account Value: $%.2f\n", totalPortfolioValue);
        System.out.println("==================================================\n");
    }
}

public class StockTradingPlatform {
    private static Map<String, Stock> marketData = new HashMap<>();
    private static Portfolio userPortfolio = new Portfolio(10000.0); // Starting with $10,000

    public static void main(String[] args) {
        initializeMarket();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to the Stock Trading Platform!");

        while (running) {
            System.out.println("\nSelect an Option:");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio Performance");
            System.out.println("5. Exit");
            System.out.print("Enter choice (1-5): ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> displayMarketData();
                case 2 -> handleBuy(scanner);
                case 3 -> handleSell(scanner);
                case 4 -> userPortfolio.displayPortfolio(marketData);
                case 5 -> {
                    running = false;
                    System.out.println("Thank you for using the Stock Trading Platform!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void initializeMarket() {
        marketData.put("AAPL", new Stock("AAPL", "Apple Inc.", 175.50));
        marketData.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 140.25));
        marketData.put("MSFT", new Stock("MSFT", "Microsoft Corp.", 330.10));
        marketData.put("TSLA", new Stock("TSLA", "Tesla Inc.", 245.80));
    }

    private static void displayMarketData() {
        System.out.println("\n================ MARKET DATA ================");
        System.out.printf("%-10s %-20s %-10s\n", "Symbol", "Company Name", "Price");
        System.out.println("--------------------------------------------");
        for (Stock stock : marketData.values()) {
            System.out.printf("%-10s %-20s $%-9.2f\n", stock.getSymbol(), stock.getName(), stock.getPrice());
        }
        System.out.println("============================================\n");
    }

    private static void handleBuy(Scanner scanner) {
        System.out.print("Enter stock symbol to buy: ");
        String symbol = scanner.next().toUpperCase();
        if (!marketData.containsKey(symbol)) {
            System.out.println("❌ Invalid stock symbol.");
            return;
        }
        System.out.print("Enter quantity to buy: ");
        int quantity = scanner.nextInt();
        userPortfolio.buyStock(marketData.get(symbol), quantity);
    }

    private static void handleSell(Scanner scanner) {
        System.out.print("Enter stock symbol to sell: ");
        String symbol = scanner.next().toUpperCase();
        if (!marketData.containsKey(symbol)) {
            System.out.println("❌ Invalid stock symbol.");
            return;
        }
        System.out.print("Enter quantity to sell: ");
        int quantity = scanner.nextInt();
        userPortfolio.sellStock(marketData.get(symbol), quantity);
    }
}