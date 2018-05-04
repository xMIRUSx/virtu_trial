package Test.server.logic;


import Test.client.SaveClientService;
import Test.server.dataAccess.DBconnector;
import Test.shared.ClientValidator;
import Test.shared.entities.Client;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;

@WebServlet(name = "SaveClientServiceImpl", urlPatterns = {"/main/saveClient"})
public class SaveClientServiceImpl extends RemoteServiceServlet implements SaveClientService {
    @Override
    public Long saveClient(Client client){
        ArrayList<String> validationErrors = ClientValidator.validate(client);
        if (validationErrors != null){
            String msg = "Невозможно сохранить клиента:<br>";
            for (String err : validationErrors){
                msg += err + "<br>";
            }
            throw new IllegalArgumentException(msg);
        }

        if(DBconnector.getInstance().clientExists(client)){
            throw new IllegalArgumentException("Клиент с такими паспортными данными уже существует!");
        }
        return DBconnector.getInstance().saveClient(client);
    }
}