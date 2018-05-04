package Test.server.logic;


import Test.client.SaveContractService;
import Test.server.dataAccess.DBconnector;
import Test.server.validation.ContractValidator;
import Test.shared.entities.Contract;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;

@WebServlet(name = "SaveContractServiceImpl", urlPatterns = {"/main/saveContract"})
public class SaveContractServiceImpl extends RemoteServiceServlet implements SaveContractService {
    @Override
    public Void saveContract(Contract contract){
        ArrayList<String> validationErrors = ContractValidator.validate(contract);
        if (validationErrors != null){
            String msg = "Невозможно сохранить договор:<br>";
            for (String err : validationErrors){
                msg += err + "<br>";
            }
            throw new IllegalArgumentException(msg);
        }

        if(DBconnector.getInstance().contractExists(contract.getNum())){
            throw new IllegalArgumentException("Договор с таким номером уже существует!");
        }

        Long id = DBconnector.getInstance().searchEstateObject(contract.getObject());
        contract.getObject().setId(id);
        DBconnector.getInstance().insertData(contract);
        return null;
    }
}