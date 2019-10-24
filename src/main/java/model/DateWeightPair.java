package model;

import java.io.Serializable;
import java.util.Date;

public class DateWeightPair implements Serializable {

    private Date date;
    private float weight;

    public DateWeightPair(Date date, float weight) {
        this.date = date;
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setKey(Date key) {
        this.date = key;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "date=" + date +
                ", weight=" + weight;
    }
}
