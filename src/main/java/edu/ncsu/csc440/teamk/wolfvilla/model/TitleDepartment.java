package edu.ncsu.csc440.teamk.wolfvilla.model;

/**
 * Created by Adac on 10/24/2016.
 */
public class TitleDepartment {
    private String title;
    private String department;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public String getDepartment() {
        return department;
    }

    public TitleDepartment(String title, String department) {
        this.title = title;
        this.department = department;
    }
}
