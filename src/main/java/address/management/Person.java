package address.management;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@Entity
@XmlRootElement
public class Person implements Serializable {
    String firstname;
    String lastname;
    Address adress;
    Date birthday;
    int age;
    @Id
    int id;
    boolean fromSQL;

    public Person() {
        fromSQL = false;
    }

    @JsonCreator
    public Person(@JsonProperty("street") String street, @JsonProperty("housenumber") String housenumber, @JsonProperty("city") String city, @JsonProperty("country") String country,
                  @JsonProperty("postcode")int postcode, @JsonProperty("firstname") String firstname, @JsonProperty("lastname") String lastname, @JsonProperty("birthday") Date birthday){
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        fromSQL = false;
        adress = new Address(street,housenumber,city,country,postcode);
    }


    public Person(@JsonProperty("street") String street, @JsonProperty("housenumber") String housenumber, @JsonProperty("city") String city, @JsonProperty("country") String country,
                  @JsonProperty("postcode")int postcode, @JsonProperty("firstname") String firstname, @JsonProperty("lastname") String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
        fromSQL = false;
        adress = new Address(street,housenumber,city,country,postcode);
    }

    public Person(@JsonProperty("firstname") String firstname, @JsonProperty("lastname") String lastname, @JsonProperty("birthday") Date birthday){
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        fromSQL = false;
    }

    @XmlElement
    public String getFirstname(){
        return firstname;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    @XmlElement
    public String getLastname(){
        return lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    @XmlElement
    public Address getAddress(){
        return adress;
    }

    public void setAddress(Address address){
        adress = address;
    }

    @XmlElement
    public Date getBirthday(){
        return birthday;
    }

    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }

    @XmlElement
    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    @XmlElement
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}
