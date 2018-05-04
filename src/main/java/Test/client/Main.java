package Test.client;


import Test.shared.ContractInfo;
import Test.shared.entities.Contract;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.ListDataProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;


public class Main implements EntryPoint {
    // СТАРТОВАЯ ФОРМА
    private VerticalPanel mainPanel = new VerticalPanel();
    private Button createBtn;
    private Button openBtn;
    private CellTable<ContractInfo> contractTable;
    private ListDataProvider<ContractInfo> dataProvider;

    // ФОРМА СОЗДАНИЯ/РЕДАКТИРОВАНИЯ ДОГОВОРА
    ContractGUI contractGUI;

    private final ContractsInfoServiceAsync contractListService = GWT.create(ContractsInfoService.class);
    private final ContractGetterServiceAsync contractGetterService = GWT.create(ContractGetterService.class);

    // номер выделенного договора из таблицы
    private Long selectedNum = null;

    private final Date today = DateTimeFormat.getFormat("dd.MM.yyyy").parse(DateTimeFormat.getFormat("dd.MM.yyyy").format(new Date()));
  /**
   * This is the entry point method.
   */
    public void onModuleLoad() {
        contractGUI = new ContractGUI(this);
        initMainPanel();
        updateTable();
    }

    /**
     * инициализирует главную форму
     */
    private void initMainPanel(){
        contractTable = initContractTable();

        createBtn = new Button("Создать договор");
        createBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
                contractGUI.show();
            }
        });

        openBtn = new Button("Открыть договор");
        openBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (selectedNum != null){
                    getAndDisplayContract(selectedNum);
                }
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(createBtn);
        buttonPanel.add(openBtn);

        mainPanel.add(buttonPanel);
        mainPanel.add(contractTable);
        RootPanel.get("tableDiv").add(mainPanel);
    }

    /**
     * инициализирует список договоров
     */
    private CellTable<ContractInfo> initContractTable(){
        CellTable<ContractInfo> tbl = new CellTable<>();

        //самодельный обработчик двойного щелчка
        //если между двумя одиночными щелчками прошло мало времени, считаем это двойным щелчком
        tbl.addCellPreviewHandler(new Handler<ContractInfo>(){

            long lastClick=-1000;

            @Override
            public void onCellPreview(CellPreviewEvent<ContractInfo> event) {
                long clictAt = System.currentTimeMillis();
                if(event.getNativeEvent().getType().contains("click")){
                    if(clictAt-lastClick < 300) {
                        // открыть выбранный договор для просмотра
                        getAndDisplayContract(event.getValue().getNum());
                    }
                    lastClick = System.currentTimeMillis();
                }
            }
        });

        //одинарный щелчок
        tbl.addCellPreviewHandler(new Handler<ContractInfo>(){

            @Override
            public void onCellPreview(CellPreviewEvent<ContractInfo> event) {
                if(event.getNativeEvent().getType().contains("click")){
                    //запомнить номер выбранного договора
                    selectedNum = event.getValue().getNum();
                }
            }
        });

        //Создание обработчика события сортировки
        dataProvider = new ListDataProvider<ContractInfo>();
        dataProvider.addDataDisplay(tbl);
        ColumnSortEvent.ListHandler<ContractInfo> columnSortHandler = new ColumnSortEvent.ListHandler<ContractInfo>(dataProvider.getList());

        //Столбец с номером договора
        Column<ContractInfo, String> numCol = new Column<ContractInfo, String>(new TextCell()){
            @Override
            public String getValue(ContractInfo object) {
          return object.getDisplayNum();
        }
        };
        numCol.setSortable(true);
        columnSortHandler.setComparator(numCol,
                new Comparator<ContractInfo>() {
                    public int compare(ContractInfo o1, ContractInfo o2) {
                        if (o1.getNum() == o2.getNum()) {
                            return 0;
                        }
                        if (o1 != null) {
                            return (o2 != null) ? o1.getNum().compareTo(o2.getNum()) : 1;
                        }
                        return -1;
                    }
                }
        );
        tbl.addColumn(numCol, "Серия-Номер");
        tbl.setColumnWidth(numCol, 10, Style.Unit.PCT);

        //Столбец с датой заключения.
        //Значение типа Date сперва преобразовать в String и затем отобразить в таблице.
        Column<ContractInfo, String> dtCol = new Column<ContractInfo, String>(new TextCell()){
            @Override
            public String getValue(ContractInfo object) {
                Date dt = object.getContractDate();
                String value = DateTimeFormat.getFormat( "dd.MM.yyyy" ).format(dt);
                return value;
            }
        };
        dtCol.setSortable(true);
        //Для сравнения использовать исходные значения типа Date.
        columnSortHandler.setComparator(dtCol,
                new Comparator<ContractInfo>() {
                    public int compare(ContractInfo o1, ContractInfo o2) {
                        if (o1.getNum() == o2.getNum()) {
                            return 0;
                        }
                        if (o1 != null) {
                            return (o2 != null) ? o1.getContractDate().compareTo(o2.getContractDate()) : 1;
                        }
                        return -1;
                    }
                }
        );
        tbl.addColumn(dtCol, "Дата заключения");
        tbl.setColumnWidth(dtCol, 10, Style.Unit.PCT);

        // ФИО страхователя
        Column<ContractInfo, String> clientCol = new Column<ContractInfo, String>(new TextCell()){
            @Override
            public String getValue(ContractInfo object) {
          return object.getClientFullName();
        }
        };
        clientCol.setSortable(true);
        columnSortHandler.setComparator(clientCol,
                new Comparator<ContractInfo>() {
                    public int compare(ContractInfo o1, ContractInfo o2) {
                        if (o1.getNum() == o2.getNum()) {
                            return 0;
                        }
                        if (o1 != null) {
                            return (o2 != null) ? o1.getClientFullName().compareTo(o2.getClientFullName()) : 1;
                        }
                        return -1;
                    }
                }
        );
        tbl.addColumn(clientCol, "Страхователь");
        tbl.setColumnWidth(clientCol, 30, Style.Unit.PCT);


        //Премия
        Column<ContractInfo, Number> premiumCol = new Column<ContractInfo, Number>(new NumberCell()){
            @Override
            public Number getValue(ContractInfo object) {
        return object.getPremium();
      }
        };
        premiumCol.setSortable(true);
        columnSortHandler.setComparator(premiumCol,
                new Comparator<ContractInfo>() {
                    public int compare(ContractInfo o1, ContractInfo o2) {
                        if (o1.getNum() == o2.getNum()) {
                            return 0;
                        }
                        if (o1 != null) {
                            return (o2 != null) ? ((Float)o1.getPremium()).compareTo(o2.getPremium()) : 1;
                        }
                        return -1;
                    }
                }
        );
        tbl.addColumn(premiumCol, "Премия");
        tbl.setColumnWidth(premiumCol, 10, Style.Unit.PCT);

        //Срок действия договора
        Column<ContractInfo, String> durationCol = new Column<ContractInfo, String>(new TextCell()){
            @Override
            public String getValue(ContractInfo object) {
        return object.getDuration();
      }
        };
        durationCol.setSortable(false);
        tbl.addColumn(durationCol, "Срок действия");
        tbl.setColumnWidth(durationCol, 20, Style.Unit.PCT);

        tbl.addColumnSortHandler(columnSortHandler);

        return tbl;
    }

    /**
     * обновление списка договоров
     */
    private void updateTable() {
        dataProvider.getList().clear();
        contractListService.getConractsInfo(new AsyncCallback<ArrayList<ContractInfo>>() {
            public void onFailure(Throwable caught) {
                MessageBox.getInstance().showMessage("Произошла ошибка!!!", caught.toString(), true);
            }

            public void onSuccess(ArrayList<ContractInfo> result) {
                dataProvider.getList().addAll(result);
                contractTable.getColumnSortList().push(contractTable.getColumn(0));

            }
        });
    }

    /**
     * Получить полную информацию о договоре и отобразить его.
     * @param contractNum номер запрашиваемго договора.
     */
    private void getAndDisplayContract(long contractNum){
        contractGetterService.getContract(contractNum,
                new AsyncCallback<Contract>() {
                    public void onFailure(Throwable caught) {
                        MessageBox.getInstance().showMessage("Произошла ошибка!!!", caught.toString(), true);
                    }

                    public void onSuccess(Contract result) {
                        hide();
                        contractGUI.displayContract(result);
                    }
                });
    }

    public void show(){
        updateTable();
        mainPanel.setVisible(true);
    }

    private void hide(){
        mainPanel.setVisible(false);
    }

}
