package Test.client;

import Test.shared.entities.Client;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;

public class ClientEditBox {
    // ФОРМА СОЗДАНИЯ/РЕДАКТИРОВАНИЯ КЛИЕНТА
    private DialogBox clientEditBox;
    private TextBox firstNameEditTxt;
    private TextBox lastNameEditTxt;
    private TextBox patronymicEditTxt;
    private DateBox birthDateEditDt;
    private TextBox passportSeriesEditTxt;
    private TextBox passportNumEditTxt;
    private Button saveClientBtn;
    private Button closeClientEditBtn;

    private final SaveClientServiceAsync saveClientService = GWT.create(SaveClientService.class);

    private ContractGUI parent;
    private Long clientID = null;

    public ClientEditBox(ContractGUI parent) {
        this.parent = parent;

        clientEditBox = new DialogBox();
        clientEditBox.setText("Клиент");
        VerticalPanel mainPanel = new VerticalPanel();


        lastNameEditTxt = new TextBox();
        lastNameEditTxt.getElement().setPropertyString("placeholder", "Фамилия");
        firstNameEditTxt = new TextBox();
        firstNameEditTxt.getElement().setPropertyString("placeholder", "Имя");
        patronymicEditTxt = new TextBox();
        patronymicEditTxt.getElement().setPropertyString("placeholder", "Отчество");
        HorizontalPanel namePanel = new HorizontalPanel();
        namePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        namePanel.setSpacing(10);
        namePanel.add(new Label("ФИО"));
        namePanel.add(lastNameEditTxt);
        namePanel.add(firstNameEditTxt);
        namePanel.add(patronymicEditTxt);

        birthDateEditDt = DateBoxFactory.makeDateBox();
        HorizontalPanel birthPanel = new HorizontalPanel();
        birthPanel.setSpacing(10);
        birthPanel.add(new Label("Дата рождения"));
        birthPanel.add(birthDateEditDt);


        passportSeriesEditTxt = new TextBox();
        passportNumEditTxt = new TextBox();
        HorizontalPanel passportPanel = new HorizontalPanel();
        passportPanel.setSpacing(10);
        passportPanel.add(new Label("Паспорт"));
        passportPanel.add(new Label("Серия"));
        passportPanel.add(passportSeriesEditTxt);
        passportPanel.add(new Label("Номер"));
        passportPanel.add(passportNumEditTxt);


        saveClientBtn = new Button("Сохранить");
        saveClientBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                saveClient();
            }
        });

        closeClientEditBtn = new Button("Закрыть");
        closeClientEditBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                closeClientEditBox();
            }
        });
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(10);
        buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        buttonPanel.add(saveClientBtn);
        buttonPanel.add(closeClientEditBtn);

        mainPanel.add(namePanel);
        mainPanel.add(birthPanel);
        mainPanel.add(passportPanel);
        mainPanel.add(buttonPanel);
        clientEditBox.add(mainPanel);
    }

    public void show(){
        clientEditBox.center();
    }

    public void fillEditClientBox(Client client){
        clientID = client.getId();

        lastNameEditTxt.setText(client.getLastName());
        firstNameEditTxt.setText(client.getFirstName());
        patronymicEditTxt.setText(client.getPatronymic());
        birthDateEditDt.setValue(client.getBirthDate());
        passportSeriesEditTxt.setText(Integer.toString(client.getPassportSeries()));
        passportNumEditTxt.setText(Integer.toString(client.getPassportNumber()));
    }

    private void saveClient(){
        Client newClient = new Client();
        newClient.setId(clientID);
        newClient.setFirstName(firstNameEditTxt.getText());
        newClient.setLastName(lastNameEditTxt.getText());
        newClient.setPatronymic(patronymicEditTxt.getText());
        newClient.setBirthDate(birthDateEditDt.getValue());
        newClient.setPassportSeries(Integer.parseInt(passportSeriesEditTxt.getText()));
        newClient.setPassportNumber(Integer.parseInt(passportNumEditTxt.getText()));
        saveClientService.saveClient(
                newClient,
                new AsyncCallback<Long>() {
                    public void onFailure(Throwable caught) {
                        MessageBox.getInstance().showMessage("Произошла ошибка!!!", "Не удалось сохранить данные о клиенте.<br>"+
                                caught.getMessage(), true);
                    }

                    public void onSuccess(Long result) {
                        newClient.setId(result);
                        parent.updateClientInMainForm(newClient);
                        closeClientEditBox();
                    }
                }
        );
    }

    private void closeClientEditBox(){
        clientEditBox.hide();
        clientID = null;
        firstNameEditTxt.setText("");
        lastNameEditTxt.setText("");
        patronymicEditTxt.setText("");
        passportSeriesEditTxt.setText("");
        passportNumEditTxt.setText("");
        birthDateEditDt.setValue(null);
    }


}
