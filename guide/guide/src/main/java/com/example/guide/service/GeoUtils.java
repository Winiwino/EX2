package com.example.guide.service;

public final class GeoUtils {

    private static final double EARTH_RADIUS_METERS = 6_371_000;

    private GeoUtils() {
    }

    public static double distanceMeters(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        double latitudeDistance = Math.toRadians(endLatitude - startLatitude);
        double longitudeDistance = Math.toRadians(endLongitude - startLongitude);
        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(startLatitude)) * Math.cos(Math.toRadians(endLatitude))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_METERS * c;
    }
}
