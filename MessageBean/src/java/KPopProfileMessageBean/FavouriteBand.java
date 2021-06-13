/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileMessageBean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * This is the Entity Object to use Persistence, which references the table of user profiles and their list of favourite kpop bands
 */
@Entity
@Table(name = "jbr9093_kpop_band_faves")
public class FavouriteBand {
    
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column (name = "band_name")
    private String bandName;
    @Column
    private String username;
    
    public FavouriteBand()
    {}
    
    public Long getId()
    {
        return id;
    }
    
    public String getBandName()
    {
        return bandName;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public void setBandName(String bandName)
    {
        this.bandName = bandName;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
}
