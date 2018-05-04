package Test.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *  окошко для оповещений
 */
public class MessageBox {
    private static MessageBox instance;


    private DialogBox dialogBox;
    private Button closeButton;
    private HTML serverResponseLabel;

    private MessageBox(){
        // Create the popup dialog box
        dialogBox = new DialogBox();
        dialogBox.setAnimationEnabled(true);
        closeButton = new Button("Close");

        closeButton.getElement().setId("closeButton");
        serverResponseLabel = new HTML();
        VerticalPanel dialogVPanel = new VerticalPanel();
        dialogVPanel.addStyleName("dialogVPanel");
        dialogVPanel.add(serverResponseLabel);
        dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
        dialogVPanel.add(closeButton);
        dialogBox.setWidget(dialogVPanel);

        closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });
    }

    public static MessageBox getInstance() {
        if (instance == null){
            instance = new MessageBox();
        }
        return instance;
    }

    public void showMessage(String title, String message, boolean isError){
        dialogBox.setText(title);
        if(isError) {
            serverResponseLabel.setStyleName("messageLabelError");
        } else {
            serverResponseLabel.setStyleName("messageLabelInfo");
        }
        serverResponseLabel.setHTML(message);
        dialogBox.center();
        closeButton.setFocus(true);
    }
}
