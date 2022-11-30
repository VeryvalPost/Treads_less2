import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    static final int SIZE = 1000;

    public static void main(String[] args) throws InterruptedException {


        Runnable generateText = () -> {

            String text = generateRoute("RLRFR", 100);
            int letterR = countRight(text);
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(letterR)) {
                    sizeToFreq.put(letterR, sizeToFreq.get(letterR) + 1);
                } else {
                    sizeToFreq.put(letterR, 1);
                }
                sizeToFreq.notify();
                //System.out.println("Завершен первый поток ");
            }

        };


        Runnable countMapa = () -> {

            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                printMap(sizeToFreq);
            }
            //System.out.println("Завершен второй поток");
        };
        Thread thread2 = new Thread(countMapa);
        thread2.start();

        for (int i = 0; i <= SIZE - 1; i++) {
            Thread thread1 = new Thread(generateText);
            thread1.start();
            thread1.join();
        }
        thread2.interrupt();

    }


    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countRight(String string) {
        char[] stringMass = string.toCharArray();
        int count = 0;
        for (char r : stringMass) {
            if (r == 'R') {
                count++;
            }
        }
        return count;
    }

    public static void printMap(Map<Integer, Integer> map) {
        Map<Integer, Integer> sortedMap = map.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));

        Map.Entry<Integer, Integer> mapEntry = map.entrySet().iterator().next();
        Integer key = mapEntry.getKey();
        Integer value = mapEntry.getValue();

        System.out.print("Самое частое количество повторений -" + key + " (" + value + "  раз)" + "\n");
        //for (Map.Entry<Integer, Integer> ite : sortedMap.entrySet())
        //    System.out.print("- " + ite.getKey() + "(" + ite.getValue() + " раз)" + "\n");

    }

}


