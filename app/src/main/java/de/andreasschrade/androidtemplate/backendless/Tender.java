package de.andreasschrade.androidtemplate.backendless;

/**
 * Created by ronan.p.higgins on 25/09/2016.
 */
public class Tender {

    private String type;
    private double latitude;
    private double longitude;
    private String ownerId;
    private String objectId;

    public Tender()
    {
    }

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId( String objectId )
    {
        this.objectId = objectId;
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

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }
}
