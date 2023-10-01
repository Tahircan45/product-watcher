package watcher;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class FileReaderThread implements Runnable {
    private final HashMap<Character,BlockingQueue<Product>> blockingQueueMap;

    public FileReaderThread(HashMap<Character,BlockingQueue<Product>> blockingQueueMap) throws IOException {
        this.blockingQueueMap = blockingQueueMap;
        this.watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(PRODUCTS_PATHS);
        path.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE);
    }

    private static final String PRODUCTS_PATHS = System.getProperty("user.home") + "/products";
    private final WatchService watchService;

    @Override
    public void run() {
        try {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    String fileName = event.context().toString();
                    if(isTextFile(fileName)){
                        String firstLine = readFirstLine(fileName);
                        putQueue(firstLine, fileName);
                    }

                }
                key.reset();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String readFirstLine(String fileName) throws IOException{
        String filePath = PRODUCTS_PATHS + "/" + fileName;
        File file = new File(filePath);
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        String line = reader.readLine();
        reader.close();
        fileReader.close();
        file.delete();

        return line;
    }

    private void putQueue(String line, String fileName) throws InterruptedException {
        char fileType = fileName.charAt(0);
        String[] splitLine = line.split(":");
        String productName = splitLine[0];
        int amount  = Integer.parseInt(splitLine[1]);
        Product product = new Product(productName, amount);

        blockingQueueMap.get(fileType).put(product);
    }

    private boolean isTextFile(String fileName) {
        return (fileName.endsWith(".txt"));
    }
}
