package de.andreasschrade.androidtemplate.backendless;

/**
 * Created by ronan.p.higgins on 25/09/2016.
 */
public class Tender {

    private String time;
    private double latitude;
    private double longitude;
    private String ownerId;

    public Tender()
    {
    }

    public String getOwnerId()
    {
        return ownerId;
    }

    public void setOwnerId( String ownerId )
    {
        this.ownerId = ownerId;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude( double latitude )
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude( double longitude )
    {
        this.longitude = longitude;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime( String time )
    {
        this.time = time;
    }
}
