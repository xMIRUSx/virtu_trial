package Test.server.logic;


import Test.client.ClientListService;
import Test.server.dataAccess.*;
import Test.shared.entities.Client;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;

@WebServlet(name = "ClientListServiceImpl", urlPatterns = {"/main/getClients"})
public class ClientListServiceImpl extends RemoteServiceServlet implements ClientListService {
    @Override
    public ArrayList<Client> getClients(String firstName, String lastName, String patronymic){
        ArrayList<Client> l = (ArrayList<Client>) DBconnector.getInstance().getClientsByName(firstName, lastName, patronymic);
        return l;
    }
}