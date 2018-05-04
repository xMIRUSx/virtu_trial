package Test.server.logic;

import Test.client.ContractsInfoService;
import Test.server.dataAccess.*;
import Test.shared.ContractInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.annotation.WebServlet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ContractsSrvlt", urlPatterns = {"/main/contractsList"})
public class ContractsInfoServiceImpl extends RemoteServiceServlet implements ContractsInfoService {

    /**
     * Получить договора в краткой форме. Запаковать каждый договор в ContractInfo для отображения в GUI таблице.
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public ArrayList<ContractInfo> getConractsInfo() throws IllegalArgumentException {

        ArrayList<ContractInfo> result = new ArrayList<>();

        List<Map<String, Object>> shortContracts = DBconnector.getInstance().getContractsShort();
        for (Map<String, Object> contract : shortContracts)
        {

            //Номер договора
            Long num = (Long) contract.get("num");
            //Делаем номер с ведущими нулями.
            //Преобразование в строку производим на сервере, т.к. GWT не может транслировать метод String.format в JS.
            String displayNum = String.format("%06d", num);

            Date contractDate = (Date) contract.get("contractDate");

            //ФИО
            String clientFullName = contract.get("lastName") + " " +
                    contract.get("firstName") + " " +
                    contract.get("patronymic");

            //премия
            float premium = (float) contract.get("premium");

            //срок действия
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            Date dt1 = (Date) contract.get("startDate");
            Date dt2 = (Date) contract.get("endDate");
            String duration = df.format(dt1) + " - " + df.format(dt2);

            ContractInfo info = new ContractInfo(num, displayNum, contractDate, clientFullName, premium, duration);
            result.add(info);
        }

        return result;
    }
}