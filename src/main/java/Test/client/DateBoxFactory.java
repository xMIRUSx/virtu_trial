package Test.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Порождает объекты класса DateBox с форматом даты dd.MM.yyyy
 */
public class DateBoxFactory {
    private static final DateTimeFormat dateBoxFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
    public static DateBox makeDateBox(){
        DateBox box = new DateBox();
        box.setFormat(new DateBox.DefaultFormat(dateBoxFormat));
        box.getDatePicker().setYearArrowsVisible(true);
        return box;
    }

}
