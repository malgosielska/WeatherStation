package com.company.observer;

import com.company.Measurements;
import com.google.gson.Gson;

import java.util.*;

public class KUPA implements Observer {

    private TreeSet<String> subscribedLocations = new TreeSet<>();
    private final String login;
    private final Map<String, ArrayList<Measurements>> memorisedData;

    public KUPA(String login) {
        this.login = login;
        memorisedData = new HashMap<>();
    }

    public KUPA(Map<String, ArrayList<Measurements>> data) {
        this.memorisedData = data;
        this.login = "";
    }

    public void addLocation(String location) {
        memorisedData.put(location, new ArrayList<>());
    }

    @Override
    public void sendNotification(String loc, Measurements measurements) {
        if (!memorisedData.containsKey(loc)) {
            addLocation(loc);
        }
        memorisedData.get(loc).add(measurements);
    }

    public void printData() {
        if (memorisedData.size() == 0) {
            System.out.println("You don't subscribe any location.");
        }
        for (String location : memorisedData.keySet()) {
            if (memorisedData.get(location).size() == 0) {
                System.out.println("There are no data yet.");
            }
            System.out.println(location.toUpperCase());
            for (Measurements measurements : memorisedData.get(location)) {
                double currentTemp = measurements.getTemp();
                double currentHum = measurements.getHumidity();
                double currentPress = measurements.getPressure();
                if (currentTemp == 0) {
                    System.out.printf("%15s ", "unavailable, ");
                } else {
                    System.out.printf("%10.1f", currentTemp);
                    System.out.printf("%3s", "C, ");
                }
                if (currentHum == 0) {
                    System.out.printf("%15s ", "unavailable, ");
                } else {
                    System.out.printf("%10.1f", currentHum);
                    System.out.printf("%12s", "% humidity, ");
                }
                if (currentPress == 0) {
                    System.out.printf("%15s \n", "unavailable, ");
                } else {
                    System.out.printf("%10.1f", currentPress);
                    System.out.printf("%3s \n", "HPa");
                }
            }
        }
    }

    public String getLogin() {
        return login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KUPA kupa = (KUPA) o;
        return Objects.equals(login, kupa.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    public void printSubscribedLocations() {
        if (subscribedLocations.size() == 0) {
            System.out.println("You don't subscribe any locations");
        }
        for (String location : subscribedLocations) {
            System.out.println(location.toUpperCase());
        }
    }

    public void subscribe(String location) {
        subscribedLocations.add(location);
    }

    public void unsubscribe(String location) {
        subscribedLocations.remove(location);
    }

    public String saveToJson() {
        Gson gson = new Gson();
        return gson.toJson(memorisedData);
    }

    public Map<String, List<Double>> aggregateParameters(String location) {
        ArrayList<Double> tempList = new ArrayList<>();
        ArrayList<Double> humidityList = new ArrayList<>();
        ArrayList<Double> pressureList = new ArrayList<>();

        for (Measurements measurements : memorisedData.get(location)) {
            tempList.add(measurements.getTemp());
            humidityList.add(measurements.getHumidity());
            pressureList.add(measurements.getPressure());
        }
        Map<String, List<Double>> parametersLists = new HashMap<>();
        parametersLists.put("TEMPERATURE", tempList);
        parametersLists.put("HUMIDITY", humidityList);
        parametersLists.put("PRESSURE", pressureList);

        return parametersLists;
    }

    public double[] getStatisticsForSpecificParameter(List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        double avg = sum / values.size();
        double min = Collections.min(values);
        double max = Collections.max(values);
        return new double[]{avg, min, max};
    }

    public List<double[]> analyseValuesForAllParameters(String location) {
        Map<String, List<Double>> analysedValues = aggregateParameters(location);
        List<Double> tempList = analysedValues.get("TEMPERATURE");
        List<Double> humidityList = analysedValues.get("HUMIDITY");
        List<Double> pressureList = analysedValues.get("PRESSURE");
        List<double[]> result = new ArrayList<>();

        double[] tempStatistics = getStatisticsForSpecificParameter(tempList);
        double[] humStatistics = getStatisticsForSpecificParameter(humidityList);
        double[] pressStatistics = getStatisticsForSpecificParameter(pressureList);

        result.add(tempStatistics);
        result.add(humStatistics);
        result.add(pressStatistics);
        return result;
    }


    public void printAnalysedValues(String location) {
        List<double[]> statistics = analyseValuesForAllParameters(location);
        double[] tempStatistics = statistics.get(0);
        double[] humStatistics = statistics.get(1);
        double[] pressStatistics = statistics.get(2);
        int MAX_INDEX = 0;
        int MIN_INDEX = 1;
        int AVG_INDEX = 2;

        System.out.println("\n" + location.toUpperCase());
        if (tempStatistics[MAX_INDEX] == 0) {
            System.out.printf("%s %s %s %s %s %s\n", "TEMP: max = ", "unavailable", ", min = ", "unavailable", ", average = ", "unavailable");
        } else {
            System.out.printf("%s %.1f %s %.1f %s %.1f\n", "TEMP: max = ", tempStatistics[MAX_INDEX], "C, min = ",
                    tempStatistics[MIN_INDEX], "C, average = ", tempStatistics[AVG_INDEX]);
        }
        if (humStatistics[MAX_INDEX] == 0) {
            System.out.printf("%s %s %s %s %s %s\n", "HUMIDITY: max = ", "unavailable", ", min = ", "unavailable", ", average = ", "unavailable");
        } else {
            System.out.printf("%s %.1f %s %.1f %s %.1f\n", "HUMIDITY: max = ", humStatistics[MAX_INDEX],
                    "%, min = ", humStatistics[MIN_INDEX], "%, average = ", humStatistics[AVG_INDEX]);
        }
        if (pressStatistics[MAX_INDEX] == 0) {
            System.out.printf("%s %s %s %s %s %s\n", "PRESSURE: max = ", "unavailable", ", min = ", "unavailable", ", average = ", "unavailable");
        } else {
            System.out.printf("%s %.1f %s %.1f %s %.1f\n", "PRESSURE: max = ", pressStatistics[MAX_INDEX], "HPa, min = ",
                    pressStatistics[MIN_INDEX], "HPa, average = ", pressStatistics[AVG_INDEX]);
        }
    }

    public void analyse() {
        if (subscribedLocations.size() == 0) {
            System.out.println("You don't subscribe any locations");
        }
        for (String location : subscribedLocations) {
            printAnalysedValues(location);
        }
    }

    public TreeSet<String> getSubscribedLocations() {
        return subscribedLocations;
    }
}
