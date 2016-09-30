package de.andreasschrade.androidtemplate.backendless;

/**
 * Created by ronan.p.higgins on 26/09/2016.
 */
public class Bid {


    private String ownerId;
    private String objectId;
    private String tender;
    private String pickupline;

    public Bid()
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


    public String getTender()
    {
        return tender;
    }

    public void setTender( String tender )
    {
        this.tender = tender;
    }

    public String getPickupline()
    {
        return pickupline;
    }

    public void setPickupline( String pickupline )
    {
        this.pickupline = pickupline;
    }




}
