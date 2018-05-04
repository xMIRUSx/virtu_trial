package Test.shared.entities;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name="estate_objects")
public class EstateObject implements Serializable {

    @Generated(GenerationTime.INSERT)
    @Column(name="id", insertable=false, updatable = false)
    private Long id;

    @EmbeddedId
    private EstateObjectPK addressPK;

    @Column(name="mail_index")
    private String index;

    @Column(name="district")
    private String district;

    @Column(name="housing")
    private String housing;

    @Column(name="building")
    private String building;


    @Column(name="estate_type")
    private String type;

    @Column(name="build_year")
    private int buildYear;

    @Column(name="area")
    private float area;


    public EstateObject() {
    }


    public EstateObjectPK getAddressPK() {
        return addressPK;
    }

    public void setAddressPK(EstateObjectPK addressPK) {
        this.addressPK = addressPK;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(int buildYear) {
        this.buildYear = buildYear;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }


    public String getCountry() {
        return addressPK.getCountry();
    }

//    public void setCountry(String country) {
//        addressPK.setCountry(country);
//    }

    public String getRegion() {
        return addressPK.getRegion();
    }

//    public void setRegion(String region) {
//        this.region = region;
//    }

    public String getLocality() {
        return addressPK.getLocality();
    }

//    public void setLocality(String locality) {
//        this.locality = locality;
//    }

    public String getStreet() {
        return addressPK.getStreet();
    }

//    public void setStreet(String street) {
//        this.street = street;
//    }

    public Integer getStreetNum() {
        return addressPK.getStreetNum();
    }

//    public void setStreetNum(Integer streetNum) {
//        this.streetNum = streetNum;
//    }

    public Integer getAptNum() {
        return addressPK.getAptNum();
    }

//    public void setAptNum(Integer aptNum) {
//        this.aptNum = aptNum;
//    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
