package com.company.observer;

import com.company.Measurements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class KUPATest {

    private final Map<String, ArrayList<Measurements>> data = new HashMap<>();
    KUPA kupa;
    TreeSet<String> testSubscribedLocations = new TreeSet<>();
    private final String wroclaw = "Wroclaw";


    @Before
    public void init(){

        Measurements m1 = new Measurements(23.5, 14.7, 1010.2);
        Measurements m2 = new Measurements(13.5, 46.7, 990.2);
        Measurements m3 = new Measurements(-4.2, 2.7, 1122.2);

        data.put("Wroclaw", new ArrayList<>());
        data.get("Wroclaw").add(m1);
        data.get("Wroclaw").add(m2);
        data.get("Wroclaw").add(m3);

        kupa = new KUPA(data);
    }

    @Test
    public void shouldAddLocationToSubscribedLocations() {
        testSubscribedLocations.add(wroclaw);
        kupa.subscribe(wroclaw);
        Assert.assertTrue(kupa.getSubscribedLocations().containsAll(testSubscribedLocations));
        Assert.assertTrue(testSubscribedLocations.containsAll(kupa.getSubscribedLocations()));
    }

    @Test
    public void shouldRemoveLocationFromSubscribedLocations() {
        shouldAddLocationToSubscribedLocations();
        testSubscribedLocations.remove(wroclaw);
        kupa.unsubscribe(wroclaw);
        Assert.assertTrue(kupa.getSubscribedLocations().containsAll(testSubscribedLocations));
        Assert.assertTrue(testSubscribedLocations.containsAll(kupa.getSubscribedLocations()));
    }

    @Test
    public void shouldAgregateDataParemeters(){
        Map<String, List<Double>> parametersLists = kupa.aggregateParameters("Wroclaw");

        List<Double> testTempList = data.get("Wroclaw").stream().map(Measurements::getTemp).collect(Collectors.toList());
        List<Double> generatedTempList = parametersLists.get("TEMPERATURE");
        for (Double temp : testTempList) {
            Assert.assertTrue(generatedTempList.contains(temp));
        }

        List<Double> testHumidityList = data.get("Wroclaw").stream().map(Measurements::getHumidity).collect(Collectors.toList());
        List<Double> generatedHumidityList = parametersLists.get("HUMIDITY");
        for (Double humidity : testHumidityList){
            Assert.assertTrue(generatedHumidityList.contains(humidity));
        }

        List<Double> testPressureList = data.get("Wroclaw").stream().map(Measurements::getPressure).collect(Collectors.toList());
        List<Double> generatedPressureList = parametersLists.get("PRESSURE");
        for (Double pressure : testPressureList){
            Assert.assertTrue(generatedPressureList.contains(pressure));
        }
    }

    @Test
    public void shouldReturnJsonBasedOnData(){
        String expectedJson = "{\"Wroclaw\":[{\"temp\":23.5,\"humidity\":14.7,\"pressure\":1010.2}," +
                "{\"temp\":13.5,\"humidity\":46.7,\"pressure\":990.2}," +
                "{\"temp\":-4.2,\"humidity\":2.7,\"pressure\":1122.2}]}";
        String json = kupa.saveToJson();
        Assert.assertEquals(json, expectedJson);
    }

}