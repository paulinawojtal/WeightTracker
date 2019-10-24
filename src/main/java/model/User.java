package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class User implements Serializable {

    private String name;
    private ArrayList<DateWeightPair> dateWeightPairList;

    public User(String name) {
        this.name = name;
        this.dateWeightPairList = new ArrayList<DateWeightPair>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User " + name + ", weight = " + dateWeightPairList;
    }
}
