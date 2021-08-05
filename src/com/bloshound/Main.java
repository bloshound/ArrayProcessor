package com.bloshound;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        new Main().function(7);
    }

    public int[][] function(int n) {

        List<int[]> randomIntArraysList = randomIntArraysGenerator(n);                            // генерируем случайности
        Map<Boolean, List<int[]>> oddsAndEvensArrays = evensOddsSplitter(randomIntArraysList);    // разбиваем на четное и нечетное
        return sortIntArraysEvensOdds(oddsAndEvensArrays);                                        // возвращаем массив с массивами(2-мерный)


    }

    private List<int[]> randomIntArraysGenerator(int limit) {

        Random random = new Random();

        Consumer<int[]> IntArrayProprietor = intArray -> {                                     // заполнитель случайными целыми числами для массивов
            for (int i = 0; i < intArray.length; i++) {
                intArray[i] = random.nextInt();
            }
        };

        List<int[]> ranmdomIntArraysList = random.ints(0,
                (Integer.MAX_VALUE) / (limit * 100000))                      // генерация случайных целых положительных чисел
                .distinct()                                                                    // только уникальных
                .limit(limit)                                                                  // ограничиваем их количество n
                .mapToObj(int[]::new)                                                          // создаем n массивов уникальных размеров
                .peek(IntArrayProprietor)                                                      // заполняем каждый массив случайными целыми числами
                .peek(System.out::println)
                .collect(Collectors.toList());                                                 // Собираем в список(для индексов)

        return ranmdomIntArraysList;
    }

    private Map<Boolean, List<int[]>> evensOddsSplitter(List<int[]> intArraysList) {
        AtomicInteger count = new AtomicInteger();                                             // счетчик для стрима
        return intArraysList.stream().
                collect(Collectors.partitioningBy(a -> (count.getAndIncrement() % 2 == 0)));   // в false нечетные по индексу массивы, в true - четные(и 0)
    }


    private int[][] sortIntArraysEvensOdds(Map<Boolean, List<int[]>> oddsAndEvens) {

        List<int[]> evenArrays = oddsAndEvens.get(true).stream()                                 // сортировка четных по возрастанию
                .peek(Arrays::sort)
                .collect(Collectors.toList());

        List<int[]> oddsArrays = oddsAndEvens.get(false).stream().                               // сортировка нечетных по убыванию(обратный порядок)
                map(intArray -> Arrays.stream(intArray)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray())
                .collect(Collectors.toList());

        return Stream.concat(evenArrays.stream(), oddsArrays.stream())                           //собираем все в общий двумерный массив
                .peek(x -> System.out.println(Arrays.toString(x)))
                .toArray(int[][]::new);
    }
}






