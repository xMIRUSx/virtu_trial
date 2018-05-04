package Test.server.logic;

import Test.client.PremiumCalculatorService;
import Test.server.dataAccess.*;
import Test.server.validation.PremiumCalcValidator;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.annotation.WebServlet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@WebServlet(name = "PremiumCalculatorServiceImpl", urlPatterns = {"/main/calculatePremium"})
public class PremiumCalculatorServiceImpl extends RemoteServiceServlet implements PremiumCalculatorService {
    @Override
    public Map<String, String> calculatePremium(String estateType, String buildYearStr, String areaStr, String sumStr, Date beginDt, Date endDt){

        // повторно проверить данные
        ArrayList<String> validationErrors = PremiumCalcValidator.validate(estateType, buildYearStr, areaStr, sumStr, beginDt, endDt);
        if (validationErrors != null){
            String msg = "Невозможно расчитать пермию:<br>";
            for (String err : validationErrors){
                msg += err + "<br>";
            }
            throw new IllegalArgumentException(msg);
        }

        // вычислить кол-во дней
        long diff = endDt.getTime() - beginDt.getTime();
        long days =  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        Integer buildYear = Integer.parseInt(buildYearStr);
        Float sum = Float.parseFloat(sumStr);
        Float area = Float.parseFloat(areaStr);

        //На основе значений входных данных выбрать коэффециенты
        float ec = DBconnector.getInstance().getEstateTypeCoef(estateType);
        float ac = DBconnector.getInstance().getAreaCoef(area);
        float yc = DBconnector.getInstance().getBuildYearCoef(buildYear);

        // Вычислить премию по формуле:	Страховая премия = (Страховая сумма / кол-во дней) * К1 * К2 * К3
        Float premium = (float) (ec*ac*yc * sum/days);
        // Окргулить до двух знаков в лробной части.
        int rounded = Math.round(premium * 100);
        premium = Float.valueOf(rounded) / 100;

        //Вернуть значение страховой премии с датой расчёта
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String dt = df.format(new Date());

        String premiumStr = String.format("%.02f", premium);

        Map<String, String> map = new HashMap<>();
        map.put("premium", premiumStr);
        map.put("date", dt);

        return map;
    }
}