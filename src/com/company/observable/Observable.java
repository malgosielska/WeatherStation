package com.company.observable;

import com.company.observer.KUPA;

public interface Observable {

    void register(KUPA k, String location);
    void remove(KUPA k, String location );
    void update();

}
