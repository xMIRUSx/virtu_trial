package Test.client;

import Test.shared.ContractValidator;
import Test.shared.EstateObjectValidator;
import Test.shared.PremiumCalcValidator;
import Test.shared.entities.Client;
import Test.shared.entities.Contract;
import Test.shared.entities.EstateObject;
import Test.shared.entities.EstateObjectPK;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Конейнер форма создания договора
 */
public class ContractGUI{
    private VerticalPanel contractPanel = new VerticalPanel();
    private TextBox insuranceSumTxt;
    private ListBox estateTypeLBox;
    private TextBox buildYearTxt;
    private TextBox areaTxt;
    private TextBox calcDateTxt;
    private TextBox premiumTxt;
    private DateBox begintDtBox;
    private DateBox endDtBox;
    private Button calcBtn;

    private TextBox numTxt;
    private TextBox contractDtTxt;

    private TextBox clientFullNameTxt;
    private Button clientSearchBtn;
    private Button editClientBtn;
    private TextBox birthDateTxt;
    private TextBox passportSeriesTxt;
    private TextBox passportNumTxt;

    private TextBox countryTxt;
    private TextBox indexTxt;
    private TextBox regionTxt;
    private TextBox districtTxt;
    private TextBox localityTxt;
    private TextBox streetTxt;
    private TextBox streetNumTxt;
    private TextBox housingTxt;
    private TextBox buildingTxt;
    private TextBox aptNumTxt;

    private TextArea noteTxt;

    private Button saveContractBtn;
    private Button showContractListBtn;

    // контейнеры для окон поиска и создания клиента
    ClientSearchBox clientSearchBox;
    ClientEditBox clientEditBox;

    private final SaveContractServiceAsync saveContractService = GWT.create(SaveContractService.class);
    private final UpdateContractServiceAsync updateContractService = GWT.create(UpdateContractService.class);
    private final PremiumCalculatorServiceAsync premiumCalculator = GWT.create(PremiumCalculatorService.class);

    // клиент отображаемый на форме
    private Client client;
    //договов отображаемый на форме
    private Contract contract;
    //признак редактирования существующего договора
    private boolean editContractFlag;

    //ссылка на объект с начальной формой
    private Main parent;

    public ContractGUI(Main parent){
        this.parent = parent;
        clientEditBox = new ClientEditBox(this);
        clientSearchBox = new ClientSearchBox(this, clientEditBox);

        // БЛОК РАСЧЁТА ПРЕМИИ
        FlexTable premiumFTable = new FlexTable();
        premiumFTable.setCellSpacing(6);
        FlexTable.FlexCellFormatter cellFormatter = premiumFTable.getFlexCellFormatter();

        insuranceSumTxt = new TextBox();
        insuranceSumTxt.addStyleName("highlight");
        insuranceSumTxt.addKeyPressHandler(KeyPressHandlerFactory.makeIntHandler());

        premiumFTable.setText(0, 0, "Страховая сумма");
        premiumFTable.setWidget(0, 1, insuranceSumTxt);

        begintDtBox = DateBoxFactory.makeDateBox();
        premiumFTable.setText(0, 2, "Срок действия с");
        premiumFTable.setWidget(0, 3, begintDtBox);

        endDtBox = DateBoxFactory.makeDateBox();
        premiumFTable.setText(0, 4, "по");
        premiumFTable.setWidget(0, 5, endDtBox);

        estateTypeLBox = new ListBox();
        estateTypeLBox.setMultipleSelect(false);
        estateTypeLBox.addItem("Комната");
        estateTypeLBox.addItem("Квартира");
        estateTypeLBox.addItem("Дом");
        estateTypeLBox.setSelectedIndex(-1);
        premiumFTable.setText(1, 0, "Тип недвижимости");
        premiumFTable.setWidget(1, 1, estateTypeLBox);

        buildYearTxt = new TextBox();

        buildYearTxt.addKeyPressHandler(KeyPressHandlerFactory.makeIntHandler(4));
        premiumFTable.setText(2, 0, "Год постройки");
        premiumFTable.setWidget(2, 1, buildYearTxt);

        areaTxt = new TextBox();
        areaTxt.addKeyPressHandler(KeyPressHandlerFactory.makeDecimalHandler(1));
        premiumFTable.setText(3, 0, "Площадь, кв.м.");
        premiumFTable.setWidget(3, 1, areaTxt);

        calcBtn = new Button("Рассчитать");
        calcBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                //Сбор введённых данных
                String buildYearStr =  buildYearTxt.getText();
                String areaStr =  areaTxt.getText().replace(",", ".");
                String sumStr =  insuranceSumTxt.getText().replace(",", ".");
                Date beginDt = begintDtBox.getDatePicker().getValue();
                Date endDt = endDtBox.getDatePicker().getValue();
                String estateType = estateTypeLBox.getSelectedItemText();

                //Валидация данных
                ArrayList<String> validationErrors = PremiumCalcValidator.validate(estateType, buildYearStr, areaStr, sumStr, beginDt, endDt);
                if (validationErrors != null){
                    String msg = new String();
                    for (String err : validationErrors){
                        msg += err + "<br>";
                    }
                    MessageBox.getInstance().showMessage("Некорректные данные", msg, true);
                    return;
                }
                //расчёт премии
                calculatePremium(estateType, buildYearStr, areaStr, sumStr, beginDt, endDt);
            }
        });
        premiumFTable.setWidget(4, 0, calcBtn);


        calcDateTxt = new TextBox();
        calcDateTxt.setReadOnly(true);
        premiumFTable.setText(5, 0, "Дата расчёта");
        premiumFTable.setWidget(5, 1, calcDateTxt);

        premiumTxt = new TextBox();
        premiumTxt.setReadOnly(true);
        premiumFTable.setText(5, 3, "Премия");
        premiumFTable.setWidget(5, 4, premiumTxt);

        DecoratorPanel premiumPanel = new DecoratorPanel();
        premiumPanel.setTitle("Расчёт");
        premiumPanel.setWidget(premiumFTable);
        contractPanel.add(premiumPanel);

        //НОМЕР И ДАТА ДОГОВОРА
        HorizontalPanel numPanel = new HorizontalPanel();
        numPanel.setSpacing(10);

        numTxt = new TextBox();
        numTxt.addKeyPressHandler(KeyPressHandlerFactory.makeIntHandler(6));
        numPanel.add(new Label("№ договора"));
        numPanel.add(numTxt);

        contractDtTxt = new TextBox();
        contractDtTxt.setReadOnly(true);

        numPanel.add(new Label("Дата заключения"));
        numPanel.add(contractDtTxt);

        contractPanel.add(numPanel);

        //СТРАХОВАТЕЛЬ
        Label lbl = new Label("Страхователь");
        lbl.getElement().getStyle().setFontSize(1.5, Style.Unit.EM);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        contractPanel.add(lbl);

        // Будет создано две строки отдельно, чтобы ячейки имели разную ширину
        FlexTable clientFTable = new FlexTable();
        FlexTable.FlexCellFormatter cf = clientFTable.getFlexCellFormatter();

        clientFullNameTxt = new TextBox();
        clientFullNameTxt.setReadOnly(true);
        clientFullNameTxt.setWidth("500px");
        clientFTable.setText(0, 0, "ФИО");
        cf.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);

        clientFTable.setWidget(0, 1, clientFullNameTxt);
        cf.setColSpan(0, 1, 5);
        cf.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);

        //кнопка вызова дочернего окна
        clientSearchBtn = new Button("Выбрать");
        clientSearchBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                clientSearchBox.show();
            }
        });
        clientFTable.setWidget(0, 2, clientSearchBtn);

        //кнопка вызова дочернего окна
        editClientBtn = new Button("Изменить");
        editClientBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                clientEditBox.fillEditClientBox(client);
                clientEditBox.show();
            }
        });
        clientFTable.setWidget(0, 3, editClientBtn);

        // Создаём две строки отдельно, чтобы ячейки имели разную ширину
        contractPanel.add(clientFTable);
        clientFTable = new FlexTable();

        birthDateTxt = new TextBox();
        birthDateTxt.setReadOnly(true);
        clientFTable.setText(1, 0, "Дата рождения");
        clientFTable.setWidget(1, 1, birthDateTxt);

        clientFTable.setText(1, 3, "Паспорт");
        cf.setColSpan(1, 3, 2);
        cf.setHorizontalAlignment(1, 3, HasHorizontalAlignment.ALIGN_RIGHT);

        passportSeriesTxt = new TextBox();
        passportSeriesTxt.setReadOnly(true);
        clientFTable.setText(1, 4, "Серия");
        clientFTable.setWidget(1, 5, passportSeriesTxt);

        passportNumTxt = new TextBox();
        passportNumTxt.setReadOnly(true);
        clientFTable.setText(1, 6, "№");
        clientFTable.setWidget(1, 7, passportNumTxt);

        contractPanel.add(clientFTable);

        //АДРЕС НЕДВИЖИМОСТИ
        lbl = new Label("Адрес недвижимости");
        lbl.getElement().getStyle().setFontSize(1.5, Style.Unit.EM);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        contractPanel.add(lbl);

        countryTxt = new TextBox();
        countryTxt.getElement().setPropertyString("placeholder", "Государство");
        countryTxt.setWidth("150px");

        indexTxt = new TextBox();
        indexTxt.getElement().setPropertyString("placeholder", "Индекс");
        indexTxt.setWidth("100px");

        regionTxt = new TextBox();
        regionTxt.getElement().setPropertyString("placeholder", "республика, край, область");
        regionTxt.setWidth("320px");

        districtTxt = new TextBox();
        districtTxt.getElement().setPropertyString("placeholder", "район");
        districtTxt.setWidth("320px");

        HorizontalPanel addressPanel = new HorizontalPanel();
        addressPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        addressPanel.setSpacing(10);
        addressPanel.add(countryTxt);
        addressPanel.add(indexTxt);
        addressPanel.add(regionTxt);
        addressPanel.add(districtTxt);
        contractPanel.add(addressPanel);

        localityTxt = new TextBox();
        localityTxt.getElement().setPropertyString("placeholder", "нас. пункт");
        localityTxt.setWidth("150px");

        streetTxt = new TextBox();
        streetTxt.getElement().setPropertyString("placeholder", "улица");
        streetTxt.setWidth("350px");

        streetNumTxt = new TextBox();
        streetNumTxt.addKeyPressHandler(KeyPressHandlerFactory.makeIntHandler());
        streetNumTxt.getElement().setPropertyString("placeholder", "дом");
        streetNumTxt.setWidth("80px");

        housingTxt = new TextBox();
        housingTxt.getElement().setPropertyString("placeholder", "корпус");
        housingTxt.setWidth("80px");

        buildingTxt = new TextBox();
        buildingTxt.getElement().setPropertyString("placeholder", "строение");
        buildingTxt.setWidth("80px");

        aptNumTxt = new TextBox();
        aptNumTxt.addKeyPressHandler(KeyPressHandlerFactory.makeIntHandler());
        aptNumTxt.getElement().setPropertyString("placeholder", "квартира");
        aptNumTxt.setWidth("80px");

        addressPanel = new HorizontalPanel();
        addressPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        addressPanel.setSpacing(10);
        addressPanel.add(localityTxt);
        addressPanel.add(streetTxt);
        addressPanel.add(streetNumTxt);
        addressPanel.add(housingTxt);
        addressPanel.add(buildingTxt);
        addressPanel.add(aptNumTxt);
        contractPanel.add(addressPanel);

        //КОММЕНТАРИЙ
        lbl = new Label("Комментарий");
        lbl.getElement().getStyle().setFontSize(1.5, Style.Unit.EM);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        contractPanel.add(lbl);

        lbl = new Label("Комментарий к договору (не печатается на полисе)");
        lbl.setWidth("150px");
        noteTxt = new TextArea();
        noteTxt.setSize("700px", "100px");
        noteTxt.getElement().getStyle().setProperty("resize", "none");

        HorizontalPanel notePanel = new HorizontalPanel();
        notePanel.setSpacing(10);
        notePanel.add(lbl);
        notePanel.add(noteTxt);
        contractPanel.add(notePanel);

        saveContractBtn = new Button();
        saveContractBtn.setText("Сохранить");
        saveContractBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                // если данные не прошли валидацию, будет брошено исключение
                try {
                    contract = createContract();
                }catch(Exception e){
                    MessageBox.getInstance().showMessage("Внимание!", "Произошла ошибка при формировании договора<br>"+
                            e.getMessage(), false);
                    return;
                }

                AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.getInstance().showMessage("Произошла ошибка!!!", "Не удалось сохранить договор.<br>" +
                                caught.getMessage(), true);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        MessageBox.getInstance().showMessage("", "Договор сохранён.", false);
                    }
                };

                if (editContractFlag) {
                    updateContractService.updateContract(contract, callback);
                } else {
                    saveContractService.saveContract(contract, callback);
                }
            }
        });

        showContractListBtn = new Button();
        showContractListBtn.setText("К списку договоров");
        showContractListBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
                parent.show();
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(50);
        buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        buttonPanel.add(saveContractBtn);
        buttonPanel.add(showContractListBtn);
        contractPanel.add(buttonPanel);


        RootPanel.get("contractDiv").add(contractPanel);
        contractPanel.setVisible(false);
    }

    /**
     * открывает пустую форму, заполняет дату договора сегодняшним днём
     */
    public void show(){
        String today = DateTimeFormat.getFormat( "dd.MM.yyyy" ).format(new Date());
        contractDtTxt.setText(today);
        contractPanel.setVisible(true);
    }

    /**
     * закрыает форму и очистить её поля
     */
    public void hide(){
        contractPanel.setVisible(false);
        clearGUI();
    }

    /**
     * заполняет форму данными о договоре и отображает её
     */
    public void displayContract(Contract contract){
        EstateObject eo = contract.getObject();

        String estateType = eo.getType();
        for(int i = 0; i < estateTypeLBox.getItemCount(); i++){
            if(estateTypeLBox.getValue(i).equalsIgnoreCase(estateType)){
                estateTypeLBox.setSelectedIndex(i);
                break;
            }
        }
        if(estateTypeLBox.getSelectedIndex() < 0){
            MessageBox.getInstance().showMessage("Ошибка!!!", "Получен неизвестный тип недвижимости.", true);
            return;
        }

        String insuraceSumStr = Float.toString(contract.getEnsuranceSum());
        insuranceSumTxt.setText(insuraceSumStr);

        String buildYearStr = Integer.toString(eo.getBuildYear());
        buildYearTxt.setText(buildYearStr);

        String areaStr = Float.toString(eo.getArea());
        areaTxt.setText(areaStr);

        Date beginDt = contract.getStartDate();
        begintDtBox.setValue(beginDt);

        Date endDt = contract.getEndDate();
        endDtBox.setValue(endDt);

        Date calcDt = contract.getPremiumCalcDate();
        String calcDtStr = DateTimeFormat.getFormat("dd.MM.yyyy").format(calcDt);
        calcDateTxt.setText(calcDtStr);

        String premiumStr = NumberFormat.getFormat(".00").format(contract.getPremium());
        premiumTxt.setText(premiumStr);

        String displayNum = NumberFormat.getFormat("000000").format(contract.getNum());
        numTxt.setText(displayNum);

        Date contDt = contract.getContractDate();
        String contDtStr = DateTimeFormat.getFormat("dd.MM.yyyy").format(contDt);
        contractDtTxt.setText(contDtStr);

        countryTxt.setText(eo.getCountry());
        indexTxt.setText(eo.getIndex());
        regionTxt.setText(eo.getRegion());
        districtTxt.setText(eo.getDistrict());
        localityTxt.setText(eo.getLocality());
        streetTxt.setText(eo.getStreet());
        housingTxt.setText(eo.getHousing());
        buildingTxt.setText(eo.getBuilding());

        String streetNumStr = eo.getStreetNum().toString();
        streetNumTxt.setText(streetNumStr);

        String aptNumStr = eo.getAptNum().toString();
        aptNumTxt.setText(aptNumStr);

        noteTxt.setText(contract.getNote());

        updateClientInMainForm(contract.getClient());

        editContractFlag = true;
        numTxt.setReadOnly(true);
        show();
    }

    public void updateClientInMainForm(Client newClient){
        client = new Client(newClient);

        clientFullNameTxt.setText(client.getFullName());
        birthDateTxt.setText(client.getBirthDateAsString());
        passportSeriesTxt.setText(Integer.toString(client.getPassportSeries()));
        passportNumTxt.setText(Integer.toString(client.getPassportNumber()));;
    }

    private void calculatePremium(String estateType, String buildYear, String area, String sum, Date beginDt, Date endDt){
        premiumCalculator.calculatePremium(
                estateType, buildYear, area, sum, beginDt, endDt,
                new AsyncCallback<Map<String, String>>() {
                    public void onFailure(Throwable caught) {
                        MessageBox.getInstance().showMessage("Произошла ошибка!!!", "Не удалось расчитать премию.<br>" +
                                caught.getMessage(), true);
                    }

                    public void onSuccess(Map<String, String> result) {
                        String dt = result.get("date");
                        String premium = result.get("premium");

                        calcDateTxt.setText(dt);
                        premiumTxt.setText(premium);
                    }
                }
        );
    }

    /**
     * Создаёт договор с данными, указанными в форме
     * @return
     */
    private Contract createContract() throws IllegalArgumentException{
        Contract c = new Contract();

        String numStr = numTxt.getText();
        String insuranceSumStr = insuranceSumTxt.getText().replace(",", ".");
        String premiumStr = premiumTxt.getText().replace(",", ".");

        ArrayList<String> validationErrors = ContractValidator.validateParams(numStr, insuranceSumStr, premiumStr);
        if (validationErrors != null){
            String msg = "";
            for (String err : validationErrors){
                msg += err + "<br>";
            }
            throw new IllegalArgumentException(msg);
        }

        c.setNum(Long.parseLong(numStr));
        c.setClient(client);
        c.setObject(createEstateObject());
        
        Date dt = DateTimeFormat.getFormat("dd.MM.yyyy").parse(contractDtTxt.getText());
        c.setContractDate(dt);
        dt = DateTimeFormat.getFormat("dd.MM.yyyy").parse(calcDateTxt.getText());
        c.setPremiumCalcDate(dt);

        c.setStartDate(begintDtBox.getDatePicker().getValue());
        c.setEndDate(endDtBox.getDatePicker().getValue());
        c.setEnsuranceSum(Float.parseFloat(insuranceSumStr));
        c.setPremium(Float.parseFloat(premiumStr));
        c.setNote(noteTxt.getText());

        return c;
    }

    /**
     * Создаёт объект недвижимости с данными, указанными в форме
     * @return
     */
    private EstateObject createEstateObject() throws IllegalArgumentException{
        String areaStr = areaTxt.getText().replace(",", ".");
        String buildYearStr = buildYearTxt.getText();
        String streetNumStr = streetNumTxt.getText();
        String aptNumStr = aptNumTxt.getText();

        ArrayList<String> validationErrors = EstateObjectValidator.validateParams(areaStr, buildYearStr, streetNumStr, aptNumStr);
        if (validationErrors != null){
            String msg = "";
            for (String err : validationErrors){
                msg += err + "<br>";
            }
            throw new IllegalArgumentException(msg);
        }

        EstateObjectPK pk = new EstateObjectPK();
        pk.setCountry(countryTxt.getText());
        pk.setRegion(regionTxt.getText());
        pk.setLocality(localityTxt.getText());
        pk.setStreet(streetTxt.getText());
        pk.setStreetNum(Integer.parseInt(streetNumStr));
        pk.setAptNum(Integer.parseInt(aptNumStr));

        EstateObject eo = new EstateObject();
        eo.setAddressPK(pk);
        eo.setIndex(indexTxt.getText());
        eo.setDistrict(districtTxt.getText());
        eo.setHousing(housingTxt.getText());
        eo.setBuilding(buildingTxt.getText());

        eo.setArea(Float.parseFloat(areaStr));
        eo.setBuildYear(Integer.parseInt(buildYearStr));
        eo.setType(estateTypeLBox.getSelectedItemText());

        return eo;
    }

    private void clearGUI(){
        editContractFlag = false;
        numTxt.setReadOnly(false);

        insuranceSumTxt.setText("");
        buildYearTxt.setText("");
        areaTxt.setText("");
        calcDateTxt.setText("");
        premiumTxt.setText("");
        begintDtBox.setValue(null);
        endDtBox.setValue(null);
        numTxt.setText("");
        contractDtTxt.setText("");
        clientFullNameTxt.setText("");
        birthDateTxt.setText("");
        passportSeriesTxt.setText("");
        passportNumTxt.setText("");
        countryTxt.setText("");
        indexTxt.setText("");
        regionTxt.setText("");
        districtTxt.setText("");
        localityTxt.setText("");
        streetTxt.setText("");
        streetNumTxt.setText("");
        housingTxt.setText("");
        buildingTxt.setText("");
        aptNumTxt.setText("");
        noteTxt.setText("");
    }
}
