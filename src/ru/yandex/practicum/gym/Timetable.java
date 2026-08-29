package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, HashSet<TrainingSession>>> timetable = new HashMap<>();
    private HashMap<Coach, Integer> counterOfTrainings = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        TreeMap<TimeOfDay, HashSet<TrainingSession>> dayTable = timetable.get(trainingSession.getDayOfWeek());
        HashSet<TrainingSession> timeOfDayTable;

        if (dayTable == null) {
            dayTable = new TreeMap<>();
            timeOfDayTable = new HashSet<>();

        } else {
            timeOfDayTable = dayTable.get(trainingSession.getTimeOfDay());

            if (timeOfDayTable == null) {
                timeOfDayTable = new HashSet<>();

            } else if (!isCoachFree(trainingSession.getCoach(), trainingSession.getDayOfWeek(), trainingSession.getTimeOfDay())) {
                return;
            }
        }

        timeOfDayTable.add(trainingSession);
        dayTable.put(trainingSession.getTimeOfDay(), timeOfDayTable);
        timetable.put(trainingSession.getDayOfWeek(), dayTable);

        counterOfTrainings.put(trainingSession.getCoach(),
                (counterOfTrainings.get(trainingSession.getCoach()) == null ? 1 : counterOfTrainings.get(trainingSession.getCoach()) + 1));
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        ArrayList<TrainingSession> result = new ArrayList<>();

        if (timetable.get(dayOfWeek) != null) {
            for (TimeOfDay time : timetable.get(dayOfWeek).navigableKeySet()) {
                result.addAll(getTrainingSessionsForDayAndTime(dayOfWeek, time));
            }
        }

        return result;
    }

    public ArrayList<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        ArrayList<TrainingSession> result = new ArrayList<>();
        TreeMap<TimeOfDay, HashSet<TrainingSession>> dayTable = timetable.get(dayOfWeek);

        if (dayTable != null) {
            HashSet<TrainingSession> timeOfDayTable = dayTable.get(timeOfDay);

            if (timeOfDayTable != null) {
                result.addAll(timeOfDayTable);
            }
        }

        return result;
    }

    private boolean isCoachFree(Coach coach, DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        for (TrainingSession session : getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay)) {
            if (session.getCoach().equals(coach)) {
                return false;
            }
        }
        return true;
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        List<CounterOfTrainings> counters = new ArrayList<>();

        for (Map.Entry<Coach, Integer> entry : counterOfTrainings.entrySet()) {
            counters.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }
        counters.sort(new CounterOfTrainingsComparator());

        return counters.reversed();
    }
}
