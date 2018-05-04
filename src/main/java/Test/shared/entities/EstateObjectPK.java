package Test.shared.entities;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EstateObjectPK implements Serializable {
    @Column(name="country")
    private String country;

    @Column(name="region")
    private String region;

    @Column(name="locality")
    private String locality;

    @Column(name="street")
    private String street;

    @Column(name="street_num")
    private Integer street_num;

    @Column(name="apt_num")
    private Integer apt_num;

    public EstateObjectPK(){};

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getStreetNum() {
        return street_num;
    }

    public void setStreetNum(Integer streetNum) {
        this.street_num = streetNum;
    }

    public Integer getAptNum() {
        return apt_num;
    }

    public void setAptNum(Integer aptNum) {
        this.apt_num = aptNum;
    }

    public boolean equals(EstateObjectPK o){
        return this.country.equalsIgnoreCase(o.getCountry()) &&
                this.region.equalsIgnoreCase(o.getRegion()) &&
                this.locality.equalsIgnoreCase(o.getLocality()) &&
                this.street.equalsIgnoreCase(o.getStreet()) &&
                this.street_num.equals(o.getStreetNum()) &&
                this.apt_num.equals(o.getAptNum());
    }

    public int hashCode() {
        return this.country.hashCode() +
                this.region.hashCode() +
                this.locality.hashCode() +
                this.street.hashCode() +
                this.street_num.hashCode() +
                this.apt_num.hashCode();
    }
}
