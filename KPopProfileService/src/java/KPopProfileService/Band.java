/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class contains band get and set information. Class is used to work with
 * our Database table called KPop_Bands
 */
@Entity
@Table(name = "KPop_Bands")
public class Band implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "generation")
    private int generation;
    @Column(name = "year")
    private int year;
    @Column(name = "fanName")
    private String fandomName;

    public Band() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getFandomName() {
        return fandomName;
    }

    public void setFandomName(String fandomName) {
        this.fandomName = fandomName;
    }

}
