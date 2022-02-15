package com.company.observer;
import com.company.Measurements;
import com.google.gson.Gson;

import java.util.*;

// KUPA - Katalog Uniwersalnych Pomiarow Asynchronicznych 

public class KUPA implements Observer {

    TreeSet <String> subscribedLocations = new TreeSet<>();
    private final String login;
    private final Map<String, ArrayList<Measurements>> data;

    public KUPA(String login) {
        this.login = login;
        data = new HashMap<>();
    }

    public KUPA(Map<String, ArrayList<Measurements>> data){
        this.data = data;
        this.login = "";
    }

    public void addLocation(String location){
        data.put(location, new ArrayList<>());
    }

    @Override
    public void sendNotification(String loc,Measurements measurements) {
        if (!data.containsKey(loc)){
            addLocation(loc);
        }
        data.get(loc).add(measurements);
    }

    public void printData() {
        if (data.size() == 0){
            System.out.println("You don't subscribe any location.");
        }
        for (String location : data.keySet()){
            if (data.get(location).size() == 0){
                System.out.println("There are no data yet.");
            }
            System.out.println(location.toUpperCase());
            for (Measurements measurements : data.get(location)){
                if (measurements.getTemp() == 0){
                    System.out.printf("%15s " , "unavailable, ");
                } else {
                    System.out.printf("%10.1f", measurements.getTemp());
                    System.out.printf("%3s", "C, ");
                }
                if (measurements.getHumidity() == 0){
                    System.out.printf("%15s " , "unavailable, ");
                } else {
                    System.out.printf("%10.1f", measurements.getHumidity());
                    System.out.printf("%12s", "% humidity, ");
                }
                if (measurements.getPressure() == 0){
                    System.out.printf("%15s \n" , "unavailable, ");
                } else {
                    System.out.printf("%10.1f", measurements.getPressure());
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
        if (subscribedLocations.size() == 0){
            System.out.println("You don't subscribe any locations");
        }
        for (String location : subscribedLocations){
            System.out.println(location.toUpperCase());
        }
    }

    public void subscribe(String location){
        subscribedLocations.add(location);
    }

    public void unsubscribe(String location){
        subscribedLocations.remove(location);
    }

    public String saveToJson() {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public Map<String, List<Double>> aggregateParameters(String location) {

        ArrayList<Double> tempList = new ArrayList<>();
        ArrayList<Double> humidityList = new ArrayList<>();
        ArrayList<Double> pressureList = new ArrayList<>();

        for (Measurements measurements : data.get(location)) {
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

    public void printAnalysedValues(String location) {

        Map<String, List<Double>> analysedValues = aggregateParameters(location);

        List<Double> tempList = analysedValues.get("TEMPERATURE");
        List<Double> humidityList = analysedValues.get("HUMIDITY");
        List<Double> pressureList = analysedValues.get("PRESSURE");

        double sumT = 0;
        for (double temp : tempList){
            sumT += temp;
        }

        double sumH = 0;
        for (double humidity : humidityList){
            sumH += humidity;
        }
        double sumP = 0;
        for (double pressure : pressureList){
            sumP += pressure;
        }

        System.out.println( "\n" + location.toUpperCase());
        if (Collections.max(tempList) == 0) {
            System.out.printf("%s %s %s %s %s %s\n", "TEMP: max = ", "unavailable", ", min = ", "unavailable", ", average = " ,"unavailable");
        } else {
            System.out.printf("%s %.1f %s %.1f %s %.1f\n", "TEMP: max = ", Collections.max(tempList), "C, min = ", Collections.min(tempList), "C, average = " ,sumT/ tempList.size());
        }
        if (Collections.max(humidityList) == 0){
            System.out.printf("%s %s %s %s %s %s\n", "HUMIDITY: max = ", "unavailable", ", min = ", "unavailable", ", average = " ,"unavailable");
        } else {
            System.out.printf("%s %.1f %s %.1f %s %.1f\n", "HUMIDITY: max = ", Collections.max(humidityList), "%, min = ", Collections.min(humidityList), "%, average = " , sumH/ humidityList.size());
        }
        if (Collections.max(pressureList) == 0){
            System.out.printf("%s %s %s %s %s %s\n", "PRESSURE: max = ", "unavailable", ", min = ", "unavailable", ", average = " ,"unavailable");
        } else {
            System.out.printf("%s %.1f %s %.1f %s %.1f\n", "PRESSURE: max = ", Collections.max(pressureList), "HPa, min = ", Collections.min(pressureList), "HPa, average = " , sumP/ pressureList.size());
        }
    }

    public void analyse(){
        if (subscribedLocations.size() == 0){
            System.out.println("You don't subscribe any locations");
        }
        for (String location : subscribedLocations){
            printAnalysedValues(location);
        }
    }

    public TreeSet<String> getSubscribedLocations() {
        return subscribedLocations;
    }
}
