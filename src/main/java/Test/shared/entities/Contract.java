package Test.shared.entities;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="contracts")
public class Contract implements Serializable {

    @Id
    @Column(name = "num", updatable = true, nullable = false)
    private Long num;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    @ManyToOne(fetch=FetchType.EAGER)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinColumn(name="estate_object_id", referencedColumnName = "id")
    private EstateObject object;

    @Column(name="contract_date")
    private Date contractDate;

    @Column(name="begin_date")
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @Column(name="ensurance_sum")
    private Float ensuranceSum;

    @Column(name="premium")
    private Float premium;

    @Column(name="premium_calc_date")
    private Date premiumCalcDate;

    @Column(name="note")
    private String note;

    public Contract() {
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public EstateObject getObject() {
        return object;
    }

    public void setObject(EstateObject object) {
        this.object = object;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public float getEnsuranceSum() {
        return ensuranceSum;
    }

    public void setEnsuranceSum(float ensuranceSum) {
        this.ensuranceSum = ensuranceSum;
    }

    public float getPremium() {
        return premium;
    }

    public void setPremium(float premium) {
        this.premium = premium;
    }

    public Date getPremiumCalcDate() {
        return premiumCalcDate;
    }

    public void setPremiumCalcDate(Date premiumCalcDate) {
        this.premiumCalcDate = premiumCalcDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
