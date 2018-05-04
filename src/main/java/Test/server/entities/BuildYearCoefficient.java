package Test.server.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="build_year_coeff")
public class BuildYearCoefficient implements Serializable {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name="build_year_lb")
    private int buildYearLB;

    @Column(name="build_year_ub")
    private int buildYearUB;

    @Column(name="coefficient")
    private float coefficient;

    public BuildYearCoefficient() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getBuildYearLB() {
        return buildYearLB;
    }

    public void setBuildYearLB(int buildYearLB) {
        this.buildYearLB = buildYearLB;
    }

    public int getBuildYearUB() {
        return buildYearUB;
    }

    public void setBuildYearUB(int buildYearUB) {
        this.buildYearUB = buildYearUB;
    }

    public float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }
}
