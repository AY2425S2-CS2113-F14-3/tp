package voyatrip;

import java.util.ArrayList;


public class Trip {

    private ArrayList<Transportation> transportations;
    private Integer transportationCount;

    public Trip() {
        this.transportations = new ArrayList<Transportation>();
        this.transportationCount = 0;
    }

    public void addTransportation(String transportMode) {
        transportations.add(new Transportation(transportMode));
        transportationCount++;
    }

    public void deleteTransportation(String transportMode) {
        for (Transportation transportation : transportations) {
            if (transportation.getMode().equals(transportMode)) {
                transportations.remove(transportation);
                transportationCount--;
            }
        }

    }
}
