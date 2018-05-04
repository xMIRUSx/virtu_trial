package Test.server.logic;


import Test.client.SaveContractService;
import Test.client.UpdateContractService;
import Test.server.dataAccess.DBconnector;
import Test.server.validation.ContractValidator;
import Test.shared.entities.Contract;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;

@WebServlet(name = "UpdateContractServiceImpl", urlPatterns = {"/main/updateContract"})
public class UpdateContractServiceImpl extends RemoteServiceServlet implements UpdateContractService {
    @Override
    public Void updateContract(Contract contract){
        ArrayList<String> validationErrors = ContractValidator.validate(contract);
        if (validationErrors != null){
            String msg = "Невозможно сохранить договор:<br>";
            for (String err : validationErrors){
                msg += err + "<br>";
            }
            throw new IllegalArgumentException(msg);
        }

        Long id = DBconnector.getInstance().searchEstateObject(contract.getObject());
        contract.getObject().setId(id);
        DBconnector.getInstance().updateData(contract);
        return null;
    }
}