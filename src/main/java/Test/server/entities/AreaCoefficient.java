package Test.server.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="area_coeff")
public class AreaCoefficient implements Serializable {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name="area_lb")
    private float areaLB;

    @Column(name="area_ub")
    private float areaUB;

    @Column(name="coefficient")
    private float coefficient;

    public AreaCoefficient() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getAreaLB() {
        return areaLB;
    }

    public void setAreaLB(float areaLB) {
        this.areaLB = areaLB;
    }

    public float getAreaUB() {
        return areaUB;
    }

    public void setAreaUB(float areaUB) {
        this.areaUB = areaUB;
    }

    public float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }
}
