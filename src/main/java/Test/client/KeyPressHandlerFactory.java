package Test.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;


public class KeyPressHandlerFactory {

    /**
     * Создать обработчик клавиш, допускающий ввод только целых чисел
     * @return
     */
    public static KeyPressHandler makeIntHandler(){
        KeyPressHandler keyHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                TextBox textBox = (TextBox) event.getSource();
                if (!Character.isDigit(event.getCharCode()) &&
                        event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB &&
                        event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE
                        ){
                    textBox.cancelKey();
                }
            }
        };
        return keyHandler;
    }

    /**
     * Создать обработчик клавиш, допускающий ввод только целых чисел
     * @param maxLen максимальная длина числа
     * @return
     */
    public static KeyPressHandler makeIntHandler(final int maxLen){
        KeyPressHandler keyHandler = new KeyPressHandler() {
            //private final int maxLength = maxLen;
            @Override
            public void onKeyPress(KeyPressEvent event) {
                TextBox textBox = (TextBox) event.getSource();
                if (!Character.isDigit(event.getCharCode())&&
                        event.getNativeEvent().getKeyCode() != KeyCodes.KEY_TAB &&
                        event.getNativeEvent().getKeyCode() != KeyCodes.KEY_BACKSPACE
                        ){
                        textBox.cancelKey();
                } else if (!(textBox.getText().length() < maxLen)) {
                    textBox.cancelKey();
                }
            }
        };
        return keyHandler;
    }

    /**
     * Создать обработчик клавиш, допускающий ввод только вещественных чисел
     * @param maxDecimalPartLen максимальное количесво знаков после запятой
     * @return
     */
    public static KeyPressHandler makeDecimalHandler(final int maxDecimalPartLen){

        KeyPressHandler keyHandler = new KeyPressHandler() {
            private final String regexp = "^\\d+[,\\.]?\\d{0,%maxLen%}$".replace("%maxLen%", Integer.toString(maxDecimalPartLen));
            @Override
            public void onKeyPress(KeyPressEvent event) {
                TextBox textBox = (TextBox) event.getSource();
                String str = textBox.getText();
                str += event.getCharCode();

                if (!str.matches(regexp)){
                    textBox.cancelKey();
                }
            }
        };
        return keyHandler;
    }

}
