package Test.client;

import Test.shared.ContractInfo;
import Test.shared.entities.Contract;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

@RemoteServiceRelativePath("contractGetter")
public interface ContractGetterService extends RemoteService {
    Contract getContract(Long contractNum) throws IllegalArgumentException;
}