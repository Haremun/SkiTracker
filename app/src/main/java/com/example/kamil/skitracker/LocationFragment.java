package com.example.kamil.skitracker;

import android.location.Location;

/**
 * Created by Kamil on 24.03.2018.
 */

public interface LocationFragment {

    void Update(Location location);
    void setAttached(boolean attach);
    void setLocation(Location location);
    boolean isAttached();

}
