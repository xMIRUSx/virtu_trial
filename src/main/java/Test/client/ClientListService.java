package Test.client;

import Test.shared.entities.Client;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

@RemoteServiceRelativePath("getClients")
public interface ClientListService extends RemoteService {
    ArrayList<Client> getClients(String firstName, String lastName, String patronymic);
}