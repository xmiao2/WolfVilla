package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Joshua on 10/24/2016.
 */
public class room_category {
    private String categoryName;
    private Integer maxOccupancy;
    private double nightly_rate;

    public room_category(String categoryName, Integer maxOccupancy,
                         double nightly_rate) {
        this.categoryName = categoryName;
        this.maxOccupancy = maxOccupancy;
        this.nightly_rate = nightly_rate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }

    public double getNightly_rate() {
        return nightly_rate;
    }

    public void setNightly_rate(double nightly_rate) {
        this.nightly_rate = nightly_rate;
    }
}
