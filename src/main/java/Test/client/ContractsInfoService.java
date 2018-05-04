package Test.client;

import Test.shared.ContractInfo;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

@RemoteServiceRelativePath("contractsList")
public interface ContractsInfoService extends RemoteService {
    ArrayList<ContractInfo> getConractsInfo() throws IllegalArgumentException;
}