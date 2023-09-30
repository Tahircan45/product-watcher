package watcher;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static HashMap<Character, BlockingQueue<Product>> blockingQueueMap = new HashMap<>();
    public static final Map<String, Integer> globalProducts = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) throws IOException {
        for (char c = 'A'; c <= 'Z'; ++c) {
            LinkedBlockingQueue<Product> processQueue = new LinkedBlockingQueue<>();
            blockingQueueMap.put(c, processQueue);
            String processName = "Process " + c;
            Thread processThread = new Thread(new ProcessThread(processQueue,processName));
            processThread.start();
        }
        Thread fileReaderThread = new Thread(new FileReaderThread(blockingQueueMap));
        fileReaderThread.start();
    }
}