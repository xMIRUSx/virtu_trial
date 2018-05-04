package Test.shared;

import java.io.Serializable;
import java.util.Date;

/**
 * Класс, содержащий краткую информацию о договоре. Для отображения в таблице на главной странице.
 */
public class ContractInfo implements Serializable{

    private Long num;
    private String displayNum;

    private Date contractDate;

    private String clientFullName;

    private float premium;

    private String duration;

    public ContractInfo() {
    }

    public ContractInfo(Long num, String displayNum, Date contractDate, String clientFullName, float premium, String duration) {
        if (num == null){
            throw new NullPointerException();
        }
        this.num = num;
        this.displayNum = displayNum;
        this.contractDate = contractDate;
        this.clientFullName = clientFullName;
        this.premium = premium;
        this.duration = duration;
    }

    public String getDisplayNum() {
        return displayNum;
    }

    public void setDisplayNum(String displayNum) {
        if (displayNum == null){
            throw new NullPointerException();
        }
        this.displayNum = displayNum;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        if (num == null){
            throw new NullPointerException();
        }
        this.num = num;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public float getPremium() {
        return premium;
    }

    public void setPremium(float premium) {
        this.premium = premium;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
