package Test.server.logic;

import Test.client.ContractGetterService;
import Test.client.ContractsInfoService;
import Test.server.dataAccess.DBconnector;
import Test.shared.ContractInfo;
import Test.shared.entities.Contract;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.annotation.WebServlet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ContractGetterSrvlt", urlPatterns = {"/main/contractGetter"})
public class ContractGetterServiceImpl extends RemoteServiceServlet implements ContractGetterService {

    @Override
    public Contract getContract(Long contractNum) throws IllegalArgumentException {
        Contract contract = DBconnector.getInstance().getContractByNum(contractNum);
        return contract;
    }
}