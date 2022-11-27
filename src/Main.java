import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {




        ExecutorService threadPool = Executors.newFixedThreadPool(1000);

        final Callable<String> textStrings = () -> {

            String text =generateRoute("RLRFR", 100);
            int letterR = countRight(text);
            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(letterR)){
                    sizeToFreq.put(letterR, sizeToFreq.get(letterR)+1);
                }else sizeToFreq.put(letterR, 1);

            }
            return text.toString();
        };

        for (int i=0; i<=1000; i++) {
            threadPool.submit(textStrings);
        }

        //System.out.println(sizeToFreq.entrySet());
       printMap(sizeToFreq);
    }


    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countRight (String string){
        char[] stringMass = string.toCharArray();
        int count = 0;
        for (char r : stringMass){
            if (r == 'R') {
                count++;
            }
        }
        return count;
    }

    public static void printMap (Map<Integer,Integer> map) {
        Map<Integer, Integer> sortedMap = map.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> { throw new AssertionError(); },
                        LinkedHashMap::new
                ));


        System.out.print("Самое частое количество повторений" );

        for(Map.Entry<Integer,Integer>ite : sortedMap.entrySet())
            System.out.print("- "+ ite.getKey()+"("+ ite.getValue()+" раз)"+"\n");

    }

}


