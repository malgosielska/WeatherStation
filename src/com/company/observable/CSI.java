package com.company.observable;
import com.company.Locations;
import com.company.Measurements;
import com.company.observer.KUPA;
import com.company.observer.Observer;

import java.util.*;

public class CSI implements Observable {

    private boolean shouldContinue = true;
    private final Thread thread;
    private final Object usersSemaphore = new Object();
    private final Locations locations = new Locations();
    private final Map<String, ArrayList<Observer> > subscribersOfLocation = new HashMap<>();

    public CSI() {

        thread = new Thread(() -> runInternal());
        addLocations();

    }

    @Override
    public void update() {
        synchronized (usersSemaphore) {
            for (String loc : subscribersOfLocation.keySet()){
                Measurements lastMeasure = measure(loc);
                if (subscribersOfLocation.get(loc).size() != 0){
                    for (Observer observer : subscribersOfLocation.get(loc)){
                        observer.sendNotification(loc, lastMeasure);
                    }
                }
            }
        }
    }

    public void runInternal() {
        while(shouldContinue) {

            update();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double measureTemp(){

        Random random = new Random();
        return random.nextDouble(1, 30);

    }

    public double measureHumidity(){

        Random random = new Random();
        return random.nextDouble(1, 100);

    }

    public double measurePressure(){

        Random random = new Random();
        return random.nextDouble(990, 1200);

    }

    public Measurements measure(String location){

        double lastTemp = 0;
        double lastHumidity = 0;
        double lastPressure = 0;

        if (getLocations().getLocWithParameters().get(location).get(0)){
           lastTemp = measureTemp();
        }
        if (getLocations().getLocWithParameters().get(location).get(1)) {
            lastHumidity = measureHumidity();
        }
        if (getLocations().getLocWithParameters().get(location).get(2)){
            lastPressure = measurePressure();
        }
        return new Measurements(lastTemp, lastHumidity, lastPressure);

    }

    @Override
    public void register(KUPA kupa, String location) {
        synchronized (usersSemaphore) {
            subscribersOfLocation.get(location).add(kupa);
            System.out.println("You've subscribed this location: " + location.toUpperCase());
        }
    }

    @Override
    public void remove(KUPA kupa, String location) {
        synchronized (usersSemaphore) {
            subscribersOfLocation.get(location).remove(kupa);
            System.out.println("You've unsubscribed this location: " + location.toUpperCase());
        }
    }

    public void startNotificationLoop() {
        thread.start();
    }

    public void stopNotifications() {
        shouldContinue = false;
    }

    public void waitFinish() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addLocations(){
        for (String loc : locations.getLocWithParameters().keySet()){
            subscribersOfLocation.put(loc, new ArrayList<>());
        }
    }

    public Locations getLocations() {
        return locations;
    }

    public Map<String, ArrayList<Observer>> getSubscribersOfLocation() {
        return subscribersOfLocation;
    }

    public boolean getShouldContinue() {
        return shouldContinue;
    }
}
