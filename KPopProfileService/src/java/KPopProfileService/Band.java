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

/** This class contains band get and set information. 
 *  Class is used to work with our Database table called KPop_Bands
 */
@Entity
@Table(name = "KPop_Bands")
public class Band implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "name")
    private String name;
    @Column(name = "generation")
    private String generation;
    @Column(name = "year")
    private String year;
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

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFandomName() {
        return fandomName;
    }
    
    public void setFandomName(String fandomName) {
        this.fandomName = fandomName;
    }
    
    public String getJSONString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{ \"bandName\":\"").append(this.getName()).append("\" , ");
        buffer.append(" \"fandomName\":\"").append(this.getFandomName()).append("\" , ");
        buffer.append(" \"generation\":\"").append(this.getGeneration()).append("\" , ");
        buffer.append(" \"year\":\"").append(this.getYear()).append("\" } ");
        return buffer.toString();
    }
}