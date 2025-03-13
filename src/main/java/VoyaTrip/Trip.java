package VoyaTrip;

/**
 * This is the trip class that will hold all the information about the trip.
 */
public class Trip {
    private String startDate;
    private String endDate;
    private Integer totalBudget;
    private Integer numDays;
    private ArrayList<Day> itineray;
    private Accommodation accommodation;
    private Transportation transportation;

    /**
     * Constructor for the trip class.
     * @param startDate the start date of the trip.
     * @param endDate the end date of the trip.
     * @param totalBudget the total budget for the trip.
     */
    public Trip(String startDate, String endDate, Integer totalBudget) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalBudget = totalBudget;
    }
}
