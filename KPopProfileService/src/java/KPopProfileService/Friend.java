/*
 * DMS Assignment 4
 * KPop Profile RESTFul Service
 * Elizabeth Cammell (18030282) & Bernadette Cruz (17985971)
 */
package KPopProfileService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "jbr9093_kpop_friends")
public class Friend {
    
 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    
    //username1 > username2 id
    @Column
    private String username1;
    
    @Column
    private String username2;

    public Friend() {
    }
    
    public Long getId() 
    {
        return id;
    }

    public String getUsername1()
    {
        return username1;
    }
    
    public String getUsername2()
    {
        return username2;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    public void setUsername1(String username1)
    {
        this.username1 = username1;
    }
    
        public void setUsername2(String username2)
    {
        this.username2 = username2;
    }
}
