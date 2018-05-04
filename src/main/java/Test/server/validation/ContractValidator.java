package Test.server.validation;

import Test.shared.entities.Contract;
import Test.shared.entities.EstateObject;
import Test.shared.entities.EstateObjectPK;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ContractValidator {
    /**
     * Валидация договора.
     * @param contract
     * @return список ошибок, или null если ошибок нет.
     */
    public static ArrayList<String> validate(Contract contract){

        ArrayList<String> errors = Test.shared.ContractValidator.validate(contract);
        if (errors == null){
            errors = new ArrayList<>();
        }
        errors.addAll(validateEstateObject(contract.getObject()));

        Date beginDt = contract.getStartDate();
        Date endDt = contract.getEndDate();
        LocalDate dateB = beginDt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateE = endDt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (dateB.compareTo(LocalDate.now()) < 0){
            errors.add("Дата начала не может быть раньше сегодняшней");
        }

        long months = ChronoUnit.MONTHS.between(dateB, dateE);
        int daydiff = dateE.getDayOfMonth() - dateB.getDayOfMonth();
        //Если месяц начала и конца совпадает, проверяем, чтобы день окончания не превышал дня начала.
        if (months > 12 |
                (months == 12 & daydiff > 0)) {
            errors.add("Длительность договора не может быть больше года");
        }


        if (errors.size() == 0){
            return null;
        } else {
            return errors;
        }
    }

    private static ArrayList<String> validateEstateObject(EstateObject eo){
        ArrayList<String> errors = new ArrayList<>();

        Integer buildYear = eo.getBuildYear();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        if (buildYear == null || buildYear < 1){
            errors.add("Не заполнен год постройки");
        } else if (buildYear > year){
            errors.add("Год постройки не может быть больше текущего года");
        }


        return errors;
    }

}
