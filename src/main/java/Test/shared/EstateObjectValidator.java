package Test.shared;

import Test.shared.entities.Contract;
import Test.shared.entities.EstateObject;
import Test.shared.entities.EstateObjectPK;

import java.util.ArrayList;
import java.util.Date;

public class EstateObjectValidator {
    /**
     * Валидация объекта недвижимости.
     * @return список ошибок, или null если ошибок нет.
     */
    public static ArrayList<String> validate(EstateObject eo){
        ArrayList<String> errors = new ArrayList<>();

        EstateObjectPK pk = eo.getAddressPK();
        String country = pk.getCountry();
        String region = pk.getRegion();
        String locality = pk.getLocality();
        String street = pk.getStreet();
        Integer streetNum = pk.getStreetNum();
        Integer aptNum = pk.getAptNum();
        
        if (country == null || country.isEmpty()){
            errors.add("Не заполнено государство");
        }
        if (region == null || region.isEmpty()){
            errors.add("Не заполнен регион");
        }
        if (locality == null || locality.isEmpty()){
            errors.add("Не заполнен нас. пункт");
        }
        if (street == null || street.isEmpty()){
            errors.add("Не заполнена улица");
        }
        if (streetNum == null || streetNum < 1){
            errors.add("Не заполнен номер дома");
        }
        if (aptNum == null || aptNum < 1){
            errors.add("Не заполнена квартира");
        }


        String type = eo.getType();
        Integer buildYear = eo.getBuildYear();
        Float area = eo.getArea();
        if (type == null || type.isEmpty()){
            errors.add("Не выбран тип недвижимости");
        }


        if (buildYear == null || buildYear < 1){
            errors.add("Не заполнен год постройки");
        }

        if (area == null || area < 1){
            errors.add("Не заполнена площадь");
        }

        if (errors.size() == 0){
            return null;
        } else {
            return errors;
        }
    }

    public static ArrayList<String> validateParams(String areaStr, String buildYearStr, String streetNumStr, String aptNumStr){
        ArrayList<String> errors = new ArrayList<>();

        if(areaStr == null || areaStr.isEmpty()){
            errors.add("Не заполнена площадь");
        } else {
            try {
                Float area = Float.parseFloat(areaStr);
                if (area < 0) {
                    errors.add("Площадь не может быть отрицательной");
                }
            } catch (NumberFormatException e) {
                errors.add("Площадь не удалось преобразовать в число");
            }
        }

        if(buildYearStr == null || buildYearStr.isEmpty()){
            errors.add("Не заполнен год постройки");
        } else {
            try {
                Integer buildYear = Integer.parseInt(buildYearStr);
                if (buildYear < 0) {
                    errors.add("Год постройки не может быть отрицательным");
                }
            } catch (NumberFormatException e) {
                errors.add("Год постройки не удалось преобразовать в число");
            }
        }

        if(streetNumStr == null || streetNumStr.isEmpty()){
            errors.add("Не заполнен номер дома");
        } else {
            try{
                Integer street = Integer.parseInt(streetNumStr);
                if (street < 0){
                    errors.add("Номер дома не может быть отрицательным");
                }
            } catch(NumberFormatException e){
                errors.add("Номер дома не удалось преобразовать в число");
            }
        }

        if(aptNumStr == null || aptNumStr.isEmpty()){
            errors.add("Не заполнена квартира");
        } else {
            try{
                Integer apt = Integer.parseInt(aptNumStr);
                if (apt < 0){
                    errors.add("Квартира не может быть отрицательной");
                }
            } catch(NumberFormatException e){
                errors.add("Квартиру не удалось преобразовать в число");
            }
        }

        if (errors.size() == 0){
            return null;
        } else {
            return errors;
        }
    }

}
