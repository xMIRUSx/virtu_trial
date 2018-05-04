package Test.shared;

import Test.shared.entities.Contract;
import Test.shared.entities.EstateObject;
import Test.shared.entities.EstateObjectPK;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PremiumCalcValidator {
    /**
     * Параметров расчёта премии.
     * @return список ошибок, или null если ошибок нет.
     */
    public static ArrayList<String> validate(String estateType, String buildYear, String area, String sum, Date beginDt, Date endDt){
        ArrayList<String> errors = new ArrayList<>();

        if (beginDt == null || endDt == null){
            errors.add("Не указан период действия договора");
        }   else if (beginDt.compareTo(endDt) >= 0){
            errors.add("Дата окончания должна быть позже даты начала");
        }

        if (estateType == null || estateType.isEmpty()){
            errors.add("Не выбран тип недвижимости");
        }


        if (buildYear == null || buildYear.isEmpty()){
            errors.add("Не заполнен год постройки");
        }

        if (area == null || area.isEmpty()){
            errors.add("Не заполнена площадь");
        } else {
            try{
                Float a = Float.parseFloat(area);
                if (a <= 0){
                    errors.add("Площадь должна быть больше нуля");
                }
            } catch(NumberFormatException e){
                errors.add("Площадь не удалось преобразовать в число");
            }
        }

        if (sum == null || sum.isEmpty()){
            errors.add("Не заполнена страховая сумма");
        } else {
            try{
                Integer s = Integer.parseInt(sum);
                if (s <= 0){
                    errors.add("Cтраховая сумма должна быть больше нуля");
                }
            } catch(NumberFormatException e){
                errors.add("Cтраховую сумму не удалось преобразовать в число");
            }
        }

        if (errors.size() == 0){
            return null;
        } else {
            return errors;
        }
    }
}
