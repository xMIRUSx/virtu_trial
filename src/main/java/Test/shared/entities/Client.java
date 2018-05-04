package Test.shared.entities;

import com.google.gwt.i18n.shared.DateTimeFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="clients")
public class Client implements Serializable {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="patronymic")
    private String patronymic;

    @Column(name="birth_date")
    private Date birthDate;

    @Column(name="passport_series")
    private Integer passportSeries;

    @Column(name="passport_number")
    private Integer passportNumber;

    public Client(){

    }

    public Client(Client anotherClient){
        this.id = anotherClient.getId();
        this.firstName = anotherClient.getFirstName();
        this.lastName = anotherClient.getLastName();
        this.patronymic = anotherClient.getPatronymic();
        this.birthDate = anotherClient.getBirthDate();
        this.passportSeries = anotherClient.getPassportSeries();
        this.passportNumber = anotherClient.getPassportNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getFullName(){
        return lastName + " " + firstName + " " + patronymic;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getBirthDateAsString(){
        return DateTimeFormat.getFormat( "dd.MM.yyyy" ).format(birthDate);
    }


    public void setPassportSeries(int passportSeries) {
        this.passportSeries = passportSeries;
    }

    public int getPassportSeries() {
        return passportSeries;
    }

    public void setPassportNumber(int passportNumber) {
        this.passportNumber = passportNumber;
    }

    public int getPassportNumber() {
        return passportNumber;
    }
}
