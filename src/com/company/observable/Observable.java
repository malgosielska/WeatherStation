package com.company.observable;

import com.company.observer.KUPA;

public interface Observable {

    public void register(KUPA k, String location);
    public void remove(KUPA k, String location );
    public void update();

}
