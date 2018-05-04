package Test.client;

import Test.shared.entities.Client;
import Test.shared.entities.Contract;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ListDataProvider;

import java.util.ArrayList;
import java.util.Comparator;

public class ClientSearchBox {
    private DialogBox clientSearchBox;
    private TextBox firstNameSearchTxt;
    private TextBox lastNameSearchTxt;
    private TextBox patronymicSearchTxt;
    private CellTable<Client> clientTable;
    private ListDataProvider<Client> clientDataProvider;
    private Button searchClientBtn;
    private Button selectClientBtn;
    private Button newClientBtn;
    private Button closeClientSearchBtn;

    private final ClientListServiceAsync clientListService = GWT.create(ClientListService.class);

    private ContractGUI parent;
    private ClientEditBox clientEditBox;
    private Client selectedClient;

    public ClientSearchBox(ContractGUI parent, ClientEditBox clientEditBox){
        this.parent = parent;
        this.clientEditBox = clientEditBox;

        initClientTable();

        clientSearchBox = new DialogBox();
        clientSearchBox.setTitle("Выбор клиента");
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        lastNameSearchTxt = new TextBox();
        lastNameSearchTxt.getElement().setPropertyString("placeholder", "Фамилия");
        firstNameSearchTxt = new TextBox();
        firstNameSearchTxt.getElement().setPropertyString("placeholder", "Имя");
        patronymicSearchTxt = new TextBox();
        patronymicSearchTxt.getElement().setPropertyString("placeholder", "Отчество");
        searchClientBtn = new Button("search"); //TODO set image

        searchClientBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String firstName = firstNameSearchTxt.getText();
                String lastName = lastNameSearchTxt.getText();
                String patronymic = patronymicSearchTxt.getText();
                searchClients(firstName, lastName, patronymic);
            }
        });
        HorizontalPanel namePanel = new HorizontalPanel();
        namePanel.setSpacing(10);
        namePanel.add(new Label("ФИО"));
        namePanel.add(lastNameSearchTxt);
        namePanel.add(firstNameSearchTxt);
        namePanel.add(patronymicSearchTxt);
        namePanel.add(searchClientBtn);

        selectClientBtn = new Button("Выбрать");
        selectClientBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                parent.updateClientInMainForm(selectedClient);
                closeClientSearchBox();
            }
        });

        newClientBtn = new Button("Новый");
        newClientBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                closeClientSearchBox();
                clientEditBox.show();
            }
        });
        closeClientSearchBtn = new Button("Закрыть");
        closeClientSearchBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                closeClientSearchBox();
            }
        });
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(10);
        buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        buttonPanel.add(selectClientBtn);
        buttonPanel.add(newClientBtn);
        buttonPanel.add(closeClientSearchBtn);

        mainPanel.add(namePanel);
        mainPanel.add(clientTable);
        mainPanel.add(buttonPanel);
        clientSearchBox.add(mainPanel);
    }

    private void initClientTable(){
        clientTable = new CellTable<>();

        //самодельный обработчик двойного щелчка
        clientTable.addCellPreviewHandler(new CellPreviewEvent.Handler<Client>(){

            long lastClick=-1000;

            @Override
            public void onCellPreview(CellPreviewEvent<Client> event) {
                long clictAt = System.currentTimeMillis();
                if(event.getNativeEvent().getType().contains("click")){
                    if(clictAt-lastClick < 300) { // dblclick on 2 clicks detected within 300 ms
                        parent.updateClientInMainForm(event.getValue());
                        clientSearchBox.hide();
                    }
                    lastClick = System.currentTimeMillis();
                }
            }
        });


        clientTable.addCellPreviewHandler(new CellPreviewEvent.Handler<Client>(){
            @Override
            public void onCellPreview(CellPreviewEvent<Client> event) {
                if(event.getNativeEvent().getType().contains("click")){
                    selectedClient = event.getValue();
                }
            }
        });

        //Создание обработчика события сортировки
        clientDataProvider = new ListDataProvider<>();
        clientDataProvider.addDataDisplay(clientTable);
        ColumnSortEvent.ListHandler<Client> columnSortHandler = new ColumnSortEvent.ListHandler<>(clientDataProvider.getList());

        //Столбец ФИО
        Column<Client, String> fullNameCol = new Column<Client, String>(new TextCell()){
            @Override
            public String getValue(Client object) {
                return object.getFullName();
            }
        };
        fullNameCol.setSortable(true);
        columnSortHandler.setComparator(fullNameCol,
                new Comparator<Client>() {
                    public int compare(Client o1, Client o2) {
                        if (o1.getId() == o2.getId()) {
                            return 0;
                        }
                        if (o1 != null) {
                            return (o2 != null) ? o1.getFullName().compareTo(o2.getFullName()) : 1;
                        }
                        return -1;
                    }
                }
        );
        clientTable.addColumn(fullNameCol, "ФИО");
        clientTable.setColumnWidth(fullNameCol, 30, Style.Unit.PCT);

        //Столбец с датой рождения.
        //Значение типа Date сперва преобразуем в String и затем отображаем в таблице.
        Column<Client, String> dtCol = new Column<Client, String>(new TextCell()){
            @Override
            public String getValue(Client object) {
                return object.getBirthDateAsString();
            }
        };
        dtCol.setSortable(true);
        //Для сравнения используем исходные значения типа Date.
        columnSortHandler.setComparator(dtCol,
                new Comparator<Client>() {
                    public int compare(Client o1, Client o2) {
                        if (o1.getId() == o2.getId()) {
                            return 0;
                        }
                        if (o1 != null) {
                            return (o2 != null) ? o1.getBirthDate().compareTo(o2.getBirthDate()) : 1;
                        }
                        return -1;
                    }
                }
        );
        clientTable.addColumn(dtCol, "Дата рождения");
        clientTable.setColumnWidth(dtCol, 10, Style.Unit.PCT);

        // Паспорт
        Column<Client, String> passportCol = new Column<Client, String>(new TextCell()){
            @Override
            public String getValue(Client object) {
                return object.getPassportSeries() + " " + object.getPassportNumber();
            }
        };

        passportCol.setSortable(true);
        columnSortHandler.setComparator(passportCol,
                new Comparator<Client>() {
                    public int compare(Client o1, Client o2) {
                        if (o1.getId() == o2.getId()) {
                            return 0;
                        }
                        if (o1 != null) {
                            if (o2 == null){
                                return 1;
                            }
                            Integer i1 = o1.getPassportSeries();
                            Integer i2 = o2.getPassportSeries();
                            int firstResult = i1.compareTo(i2);
                            if (firstResult == 0){
                                i1 = o1.getPassportNumber();
                                i2 = o2.getPassportNumber();
                                return i1.compareTo(i2);
                            } else {
                                return firstResult;
                            }
                        }
                        return -1;
                    }
                }
        );

        clientTable.addColumn(passportCol, "Паспортные данные");
        clientTable.setColumnWidth(passportCol, 20, Style.Unit.PCT);

        clientTable.addColumnSortHandler(columnSortHandler);
    }

    private void searchClients(String firstName, String lastName, String patronymic){
        clientDataProvider.getList().clear();
        clientListService.getClients(firstName, lastName, patronymic,
                new AsyncCallback<ArrayList<Client>>() {
                    public void onFailure(Throwable caught) {
                        MessageBox.getInstance().showMessage("Произошла ошибка!!!", caught.toString(), true);
                    }

                    public void onSuccess(ArrayList<Client> result) {
                        clientDataProvider.getList().addAll(result);
                        clientTable.getColumnSortList().push(clientTable.getColumn(0));

                    }
                });
    }

    private void closeClientSearchBox(){
        clientSearchBox.hide();
        firstNameSearchTxt.setText("");
        lastNameSearchTxt.setText("");
        patronymicSearchTxt.setText("");
        clientDataProvider.getList().clear();
    }

    public void show(){
        clientSearchBox.center();
    }

}
