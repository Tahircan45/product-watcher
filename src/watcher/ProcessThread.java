package watcher;

import java.util.HashMap;
import java.util.Map;
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
                updateProducts(product,Main.globalProducts);
                updateProducts(product,localProducts);
                printProductState(product.getName());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateProducts(Product product, Map<String,Integer> productMap) {
        Integer totalAmount = productMap.get(product.getName());
        if (totalAmount == null)
            totalAmount = 0;
        totalAmount += product.getAmount();
        productMap.put(product.getName(), totalAmount);
    }

    private void printProductState(String productName) {
        System.out.println(name+ " - Local Products ==> " +localProducts );
        System.out.println("Last updated global product ==> "+productName +" : "+Main.globalProducts.get(productName));

        System.out.println();
        System.out.println("-------------------");
        System.out.println();
    }
}