package com.mobile.test.mpdassignment;


// Name                 Matthew Cadden
// Student ID           S1508111
// Programme of Study   Computer Games (Software Development)

public class EarthquakeDataClass {

    public float latitude;
    public float longitude;
    public float magnitude;
    public int depth;
    public String date;
    public String time;
    public String location;


    public EarthquakeDataClass()
    {
        latitude = 0f;
        longitude = 0f;
        magnitude = 0f;
        depth = 0;
        date = "";
        time = "";
        location = "";
    }

    public EarthquakeDataClass(float aLatitude, float aLongitude, float aMagnitude, int aDepth, String aDate, String aTime, String aLocation)
    {
        latitude = aLatitude;
        longitude = aLongitude;
        magnitude = aMagnitude;
        depth = aDepth;
        date = aDate;
        time = aTime;
        location = aLocation;
    }


    public float getLatitude()
    {
        return latitude;
    }

    public void setLatitude(float aLatitude)
    {
        latitude = aLatitude;
    }

    public float getLongitude()
    {
        return longitude;
    }

    public void setLongitude(float aLongitude)
    {
        longitude = aLongitude;
    }

    public float getMagnitude()
    {
        return magnitude;
    }

    public void setMagnitude(float aMagnitude)
    {
        magnitude = aMagnitude;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int aDepth)
    {
        depth = aDepth;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String aDate)
    {
        date = aDate;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String aTime)
    {
        time = aTime;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String aLocation)
    {
        location = aLocation;
    }
}
