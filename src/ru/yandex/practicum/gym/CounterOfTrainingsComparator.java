package ru.yandex.practicum.gym;

import java.util.Comparator;

public class CounterOfTrainingsComparator implements Comparator<CounterOfTrainings> {
    @Override
    public int compare(CounterOfTrainings o1, CounterOfTrainings o2) {
        return o1.getCountOfTrainings() - o2.getCountOfTrainings();
    }
}
