package com.company.observable;
import com.company.observer.KUPA;
import com.company.observer.Observer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CSITest {

    CSI csi = new CSI();
    public KUPA observer;
    public String wroclaw = "Wroclaw";
    private Map<String, ArrayList<Observer> > testSubscribersOfLocation;
    private boolean testShouldContinue = true;

    @Before
    public void preparingTest () {

        testSubscribersOfLocation = new HashMap<>();
        testSubscribersOfLocation.put(wroclaw, new ArrayList<>());
        observer = new KUPA("myLogin");

    }
    @Test
    public void shouldReturnPressureFromSpecificRange() {
        double pressure = csi.measurePressure();
        Assert.assertTrue(pressure >= 980 && pressure <1200 );
    }

    @Test
    public void shouldReturnHumidityFromSpecificRange() {
        double humidity = csi.measureHumidity();
        Assert.assertTrue(humidity >= 0 && humidity < 100 );
    }

    @Test
    public void shouldReturnTempFromSpecificRange() {
        double temp = csi.measureTemp();
        Assert.assertTrue(temp >= -10 && temp <30 );
    }

    @Test
    public void shouldAddUser(){

        testSubscribersOfLocation.get(wroclaw).add(observer);
        csi.register(observer, wroclaw);
        Assert.assertTrue(testSubscribersOfLocation.get(wroclaw).containsAll(csi.getLocationToSubscribers().get(wroclaw)));
        Assert.assertTrue(csi.getLocationToSubscribers().get(wroclaw).containsAll(testSubscribersOfLocation.get(wroclaw)));

    }

    @Test
    public void shouldRemoveUserFromUsersList(){

        shouldAddUser();
        testSubscribersOfLocation.get(wroclaw).remove(observer);
        csi.remove(observer, wroclaw);
        Assert.assertTrue(testSubscribersOfLocation.get(wroclaw).containsAll(csi.getLocationToSubscribers().get(wroclaw)));
        Assert.assertTrue(csi.getLocationToSubscribers().get(wroclaw).containsAll(testSubscribersOfLocation.get(wroclaw)));

    }

    @Test
    public void shouldChangeToFalse() {
        csi.stopNotifications();
        Assert.assertNotEquals(csi.getShouldContinue(), testShouldContinue);
    }
    @Test
    public void shouldChangeToFalseAndBeEqual(){
        testShouldContinue = false;
        csi.stopNotifications();
        Assert.assertEquals(csi.getShouldContinue(), testShouldContinue);
    }
}