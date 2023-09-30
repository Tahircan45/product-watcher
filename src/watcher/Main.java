package watcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static List<BlockingQueue<Product>> blockingQueuelist = new ArrayList<>();
    public static HashMap<String,Integer> allProduct = new HashMap<>();

    public static void main(String[] args) throws IOException{
        for (char c = 'A'; c <= 'Z'; ++c) {
            LinkedBlockingQueue<Product> blockingQueue = new LinkedBlockingQueue<>();
            blockingQueuelist.add(blockingQueue);

            Thread processThread = new Thread(new ProcessThread(blockingQueue,String.valueOf(c)));
            processThread.start();
        }
        Thread fileReaderThread = new Thread(new FileReaderThread(blockingQueuelist));
        fileReaderThread.start();
    }
}