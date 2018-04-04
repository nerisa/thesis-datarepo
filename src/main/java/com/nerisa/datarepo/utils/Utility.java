package com.nerisa.datarepo.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by nerisa on 2/15/18.
 */
public class Utility {

    private static final Logger LOG = Logger.getLogger(Utility.class.getName());
    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double calculateDistance(double lat1, double lat2, double lon1,
                                  double lon2) {
        LOG.log(Level.INFO, "Calculating distance between ("+ String.valueOf(lat1) + "," +String.valueOf(lon1) + ") and (" + String.valueOf(lat2) + "," +String.valueOf(lon2) + ")");
        final int R = 6371000; // Radius of the earth in metres

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public static Long getMonumentChildResourceId(String uri){
        int lastIndexOfSlash = uri.lastIndexOf("/");
        if(lastIndexOfSlash == -1){
            return Long.valueOf(uri);
        } else {
            return Long.valueOf(uri.substring(lastIndexOfSlash + 1));
        }
    }
}
