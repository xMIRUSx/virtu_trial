package Test.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Date;
import java.util.Map;

@RemoteServiceRelativePath("calculatePremium")
public interface PremiumCalculatorService extends RemoteService {
    Map<String, String> calculatePremium(String estateType, String buildYearStr, String areaStr, String sumStr, Date beginDt, Date endDt) throws IllegalArgumentException;
}