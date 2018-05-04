package Test.client;

import Test.shared.ContractInfo;
import Test.shared.entities.Client;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

@RemoteServiceRelativePath("saveClient")
public interface SaveClientService extends RemoteService {
    Long saveClient(Client client) throws IllegalArgumentException;
}