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
 * @author Bernie
 */
@Entity
@Table(name = "jbr9093_kpop_useres")
public class UserProfile {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column
    private String username;
    
    public UserProfile()
    {
    }
    
    public Long getId()
    {
        return id;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    
    public void setId(Long Id)
    {
        this.id = id;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
}
