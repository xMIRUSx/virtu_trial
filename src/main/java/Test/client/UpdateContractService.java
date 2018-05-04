package Test.client;

import Test.shared.entities.Contract;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("updateContract")
public interface UpdateContractService extends RemoteService {
    Void updateContract(Contract contract) throws IllegalArgumentException;
}