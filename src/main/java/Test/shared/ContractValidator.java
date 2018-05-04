package Test.shared;

import Test.client.DateBoxFactory;
import Test.shared.entities.Client;
import Test.shared.entities.Contract;
import Test.shared.entities.EstateObject;
import Test.shared.entities.EstateObjectPK;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ContractValidator {
    /**
     * Валидация договора.
     * @param contract
     * @return список ошибок, или null если ошибок нет.
     */

    public static ArrayList<String> validate(Contract contract){
        ArrayList<String> errors;

        errors = EstateObjectValidator.validate(contract.getObject());

        if (errors == null){
            errors = new ArrayList<>();
        }

        Date contractDt = contract.getContractDate();
        Date beginDt = contract.getStartDate();
        Date endDt = contract.getEndDate();
        Date premiumDt = contract.getPremiumCalcDate();
        Float ensuranceSum = contract.getEnsuranceSum();
        Float premium = contract.getPremium();

        if (beginDt == null || endDt == null){
            errors.add("Не указан период действия договора");
        }   else if (beginDt.compareTo(endDt) >= 0){
            errors.add("Дата окончания должна быть позже даты начала");
        }

        if (premiumDt == null){
            errors.add("Не указана дата расчёта премии");
        }   else if (premiumDt.compareTo(new Date()) > 0) {
            errors.add("Дата расчёта премии не может быть больше сегодняшней");
        }

        if (contractDt == null){
            errors.add("Не указана дата заключения договора");
        }   else if (contractDt.compareTo(new Date()) > 0) {
            errors.add("Дата заключения договора не может быть больше сегодняшней");
        }

        if (ensuranceSum == null || ensuranceSum <= 0){
            errors.add("Некорректная страховая сумма");
        }

        if (premium == null || premium < 0){
            errors.add("Некорректная премия");
        }

        if (errors.size() == 0){
            return null;
        } else {
            return errors;
        }
    }

    public static ArrayList<String> validateParams(String numStr, String insuranceSumStr, String premiumStr){
        ArrayList<String> errors = new ArrayList<>();

        if(numStr == null || numStr.isEmpty()){
            errors.add("Не заполнен номер договора");
        } else {
            try {
                Float sum = Float.parseFloat(premiumStr);
                if (sum < 0) {
                    errors.add("Номер договора не может быть отрицательным");
                }
            } catch (NumberFormatException e) {
                errors.add("Номер договора не удалось преобразовать в число");
            }
        }

        if(insuranceSumStr == null || insuranceSumStr.isEmpty()){
            errors.add("Не заполнена страховая сумма");
        } else {
            try {
                Float sum = Float.parseFloat(premiumStr);
                if (sum < 0) {
                    errors.add("Страховая сумма не может быть отрицательной");
                }
            } catch (NumberFormatException e) {
                errors.add("Премию не удалось преобразовать в число");
            }
        }

        if(premiumStr == null || premiumStr.isEmpty()){
            errors.add("Не произведен расчёт премии");
        } else {
            try{
                Float prem = Float.parseFloat(premiumStr);
                if (prem < 0){
                    errors.add("Премия не может быть отрицательной");
                }
            } catch(NumberFormatException e){
                errors.add("Премию не удалось преобразовать в число");
            }
        }

        if (errors.size() == 0){
            return null;
        } else {
            return errors;
        }
    }
}
