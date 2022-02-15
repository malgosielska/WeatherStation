package com.company.observer;

import com.company.Measurements;

public interface Observer {

    public void sendNotification(String loc, Measurements measurements);

}
