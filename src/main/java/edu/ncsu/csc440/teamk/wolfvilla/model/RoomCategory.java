package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Joshua on 10/24/2016.
 *
 * An object to hold all the information about a RoomCategory held in the database.
 */
public class RoomCategory {
    private String categoryName;
    private Integer maxOccupancy;
    private double nightlyRate;

    public RoomCategory() {
        categoryName = "Deluxe";
        maxOccupancy = 1;
    }

    public RoomCategory(String categoryName, Integer maxOccupancy,
                        double nightly_rate) {
        this.categoryName = categoryName;
        this.maxOccupancy = maxOccupancy;
        this.nightlyRate = nightly_rate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }

    public double getNightlyRate() {
        return nightlyRate;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setMaxOccupancy(Integer maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public void setNightlyRate(double nightlyRate) {
        this.nightlyRate = nightlyRate;
    }
}
