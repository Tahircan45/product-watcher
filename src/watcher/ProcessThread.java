package watcher;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class ProcessThread implements Runnable {
    private final BlockingQueue<Product> queue;
    private final String name;
    private final HashMap<String, Integer> localProducts = new HashMap<>();

    public ProcessThread(BlockingQueue<Product> q, String name) {
        this.queue = q;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Product product;
            while ((product = queue.take()).getName() != null) {
                updateProducts(product, localProducts);
                updateProducts(product, Main.allProduct);
                printProductState();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateProducts(Product product, HashMap<String, Integer> products) {
        Integer totalAmount = products.get(product.getName());
        if (totalAmount == null)
            totalAmount = 0;
        totalAmount += product.getAmount();
        products.put(product.getName(), totalAmount);
    }

    private void printProductState() {
        System.out.println("Local - " + name);
        System.out.println(localProducts.toString());

        System.out.println("Global");
        System.out.println(Main.allProduct.toString());

        System.out.println();
        System.out.println("-------------------");
        System.out.println();
    }
}