package Test.shared;

import Test.shared.entities.Client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ClientValidator {
    /**
     * Валидация клиента.
     * @param client
     * @return список ошибок, или null если ошибок нет.
     */
    public static ArrayList<String> validate(Client client){
        ArrayList<String> errors = new ArrayList<>();

        String firstName = client.getFirstName();
        String lastName = client.getLastName();
        Date birthDt = client.getBirthDate();
        Integer passportSeries = client.getPassportSeries();
        Integer passportNumber = client.getPassportNumber();

        if (firstName == null || firstName.isEmpty()){
            errors.add("Не указано имя.");
        }
        if (lastName == null || lastName.isEmpty()){
            errors.add("Не указана фамилия.");
        }
        if (birthDt == null){
            errors.add("Не указана дата рождения.");
        }
        if (passportSeries == null || passportSeries < 0 || passportSeries > 9999){
            errors.add("Некорректная серия паспорта.");
        }
        if (passportNumber == null || passportNumber < 0 || passportNumber > 999999){
            errors.add("Некорректный номер паспорта.");
        }

        if (errors.size() == 0){
            return null;
        } else {
            return errors;
        }
    }
}
