package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Locations {

    private final Map<String, ArrayList<Boolean>> locationToParameters = new HashMap<>();
    private final ArrayList<String> locationsList;

    public Locations() {
        locationsList = new ArrayList<>();
        readLocations();
    }

    public void readLocations() {
        int LOC_INDEX = 0;
        int PARAMETERS_INDEX = 1;
        File file = new File("locations.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String input;
            while ((input = reader.readLine()) != null) {
                String[] elements = input.split("\\s+");
                String currentLocation = elements[LOC_INDEX];
                String letters = elements[PARAMETERS_INDEX];
                ArrayList<Boolean> parameters = new ArrayList<>();
                parameters.add(letters.contains("T"));
                parameters.add(letters.contains("H"));
                parameters.add(letters.contains("P"));
                locationToParameters.put(currentLocation, parameters);
            }
            locationsList.addAll(locationToParameters.keySet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printLocations() {
        int i = 1;
        for (String location : locationToParameters.keySet()) {
            System.out.println(" - " + i + " - to choose " + location);
            i++;
        }
    }

    public boolean doesMeasureASpecificMeasurement(String location, String measurement){
        int index = switch (measurement) {
            case "temp" -> 0;
            case "hum" -> 1;
            default -> 2;
        };
        return locationToParameters.get(location).get(index);
    }

    public Map<String, ArrayList<Boolean>> getLocationToParameters() {
        return locationToParameters;
    }

    public List<String> getLocationsList() {
        return locationsList;
    }
}
