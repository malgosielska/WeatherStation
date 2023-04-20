package com.company.observable;

import com.company.Locations;
import com.company.Measurements;
import com.company.observer.KUPA;
import com.company.observer.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CSI implements Observable {

    private boolean shouldContinue = true;
    private final Thread thread;
    private final Object usersSemaphore = new Object();
    private final Locations locations = new Locations();
    private final Map<String, ArrayList<Observer>> locationToSubscribers = new HashMap<>();
    private final Random random = new Random();

    public CSI() {
        thread = new Thread(this::runInternal);
        addLocations();
    }

    @Override
    public void update() {
        synchronized (usersSemaphore) {
            for (String location : locationToSubscribers.keySet()) {
                Measurements lastMeasurement = measure(location);
                ArrayList<Observer> currentSubscribers = locationToSubscribers.get(location);
                if (currentSubscribers.size() != 0) {
                    for (Observer subscriber : currentSubscribers) {
                        subscriber.sendNotification(location, lastMeasurement);
                    }
                }
            }
        }
    }

    public void runInternal() {
        while (shouldContinue) {
            update();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public double measureTemp() {
        return random.nextDouble(1, 30);
    }

    public double measureHumidity() {
        return random.nextDouble(1, 100);
    }

    public double measurePressure() {
        return random.nextDouble(990, 1200);
    }

    public Measurements measure(String location) {
        double lastTemp = 0;
        double lastHumidity = 0;
        double lastPressure = 0;

        if (getLocations().doesMeasureASpecificMeasurement(location, "temp")) {
            lastTemp = measureTemp();
        }
        if (getLocations().doesMeasureASpecificMeasurement(location, "hum")) {
            lastHumidity = measureHumidity();
        }
        if (getLocations().doesMeasureASpecificMeasurement(location, "press")) {
            lastPressure = measurePressure();
        }
        return new Measurements(lastTemp, lastHumidity, lastPressure);
    }

    @Override
    public void register(KUPA user, String location) {
        synchronized (usersSemaphore) {
            locationToSubscribers.get(location).add(user);
            System.out.println("You've subscribed this location: " + location.toUpperCase());
        }
    }

    @Override
    public void remove(KUPA user, String location) {
        synchronized (usersSemaphore) {
            locationToSubscribers.get(location).remove(user);
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

    public void addLocations() {
        for (String loc : locations.getLocationToParameters().keySet()) {
            locationToSubscribers.put(loc, new ArrayList<>());
        }
    }

    public Locations getLocations() {
        return locations;
    }

    public Map<String, ArrayList<Observer>> getLocationToSubscribers() {
        return locationToSubscribers;
    }

    public boolean getShouldContinue() {
        return shouldContinue;
    }
}
