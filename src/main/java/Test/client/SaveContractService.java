package Test.client;

import Test.shared.entities.Client;
import Test.shared.entities.Contract;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("saveContract")
public interface SaveContractService extends RemoteService {
    Void saveContract(Contract contract) throws IllegalArgumentException;
}