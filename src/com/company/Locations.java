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

    private Map<String, ArrayList<Boolean>> locWithParameters = new HashMap<>();
    private ArrayList<String> locationsList;

    public Locations(){
        locationsList = new ArrayList<>();
        readLocations();
    }

    public void readLocations(){
        File file = new File("locations.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String input;
            while((input = reader.readLine())!=null){
                String [] tokens = input.split("\\s+");
                locWithParameters.put(tokens[0], new ArrayList<>());
                String measureList = tokens[1];
                if (measureList.contains("T")){
                    locWithParameters.get(tokens[0]).add(true);
                } else {
                    locWithParameters.get(tokens[0]).add(false);
                }
                if (measureList.contains("H")){
                    locWithParameters.get(tokens[0]).add(true);
                } else {
                    locWithParameters.get(tokens[0]).add(false);
                }
                if (measureList.contains("P")){
                    locWithParameters.get(tokens[0]).add(true);
                } else {
                    locWithParameters.get(tokens[0]).add(false);
                }
            }
            locationsList.addAll(locWithParameters.keySet());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void printLocations(){
        int i = 1;
        for (String location : locWithParameters.keySet()){
            System.out.println(" - " + i  + " - to choose " + location);
            i++;
        }
    }

    public Map<String, ArrayList<Boolean>> getLocWithParameters() {
        return locWithParameters;
    }

    public List<String> getLocationsList() {
        return locationsList;
    }
}
