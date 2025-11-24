package ua.profitsoft;

import ua.profitsoft.service.BookParser;
import ua.profitsoft.service.XmlWriter;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    private static final int THREAD_COUNT = 1;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar app.jar <folder_path> <attribute>");
            return;
        }

        String folderPath = args[0];
        String attribute = args[1];

        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            System.err.println("Invalid folder path");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null || files.length == 0) {
            System.out.println("No JSON files found in folder");
            return;
        }

        long startTime = System.currentTimeMillis();

        Map<String, Integer> stats = processFiles(files, attribute);

        new XmlWriter().writeStatistics(stats, attribute);

        long endTime = System.currentTimeMillis();
        System.out.println("Processing time (" + THREAD_COUNT + " threads): "
                + (endTime - startTime) + " ms");
    }

    private static Map<String, Integer> processFiles(File[] files, String attribute) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        BookParser parser = new BookParser();

        List<Future<Map<String, Integer>>> results = new ArrayList<>();

        for (File file : files) {
            results.add(executor.submit(() -> parser.parseAndCount(file, attribute)));
        }

        Map<String, Integer> totalStats = new HashMap<>();

        for (Future<Map<String, Integer>> future : results) {
            try {
                Map<String, Integer> fileStats = future.get();
                fileStats.forEach((key, value) ->
                        totalStats.merge(key, value, Integer::sum)
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return totalStats;
    }
}
