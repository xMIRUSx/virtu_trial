package Test.server.validation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PremiumCalcValidator {
    public static ArrayList<String> validate(String estateType, String buildYear, String area, String sum, Date beginDt, Date endDt) {

        ArrayList<String> errors = Test.shared.PremiumCalcValidator.validate(estateType, buildYear, area, sum, beginDt, endDt);

        if (errors == null) {
            errors = new ArrayList<>();
        }

        LocalDate dateB = beginDt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (dateB.compareTo(LocalDate.now()) < 0){
            errors.add("Дата начала не может быть раньше сегодняшней");
        }

        LocalDate dateE = endDt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long months = ChronoUnit.MONTHS.between(dateB, dateE);
        int daydiff = dateE.getDayOfMonth() - dateB.getDayOfMonth();
        //Если месяц начала и конца совпадает, проверяем, чтобы день окончания не превышал дня начала.
        if (months > 12 |
                (months == 12 & daydiff > 0)) {
            errors.add("Длительность договора не может быть больше года");
        }

        int year = Calendar.getInstance().get(Calendar.YEAR);
        try{
            Integer by = Integer.parseInt(buildYear);
            if (by > year){
                errors.add("Год постройки не может быть больше текущего года");
            }
        } catch(NumberFormatException e){
            errors.add("Год постройки не удалось преобразовать в число");
        }

        if (errors.size() == 0){
            return null;
        } else {
            return errors;
        }
    }
}
