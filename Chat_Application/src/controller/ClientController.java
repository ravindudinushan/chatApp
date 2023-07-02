package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientController extends Thread{
    public ScrollPane TxtMassageShowFeild;
    public VBox vBox;
    public TextField TxtMassageTypeFeild;
    public AnchorPane imogiPane;
    public Label lblName;

    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    private FileChooser fileChooser;
    private File filePath;

    public void initialize() throws IOException {
        String userName = LogingFormController.userName;
        lblName.setText(userName);
        try {
            socket = new Socket("localhost", 4500);
            System.out.println("Connected...");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imogiPane.setVisible(false);
    }

    @Override
    public void run() {
        try {
            while (true) {

                String msg = reader.readLine();
                String[] tokens = msg.split(" ");
                String cmd = tokens[0];

                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i]+" ");
                }

                String[] msgToAr = msg.split(" ");
                String st = "";
                for (int i = 0; i < msgToAr.length - 1; i++) {
                    st += msgToAr[i + 1] + " ";
                }

                Text text = new Text(st);
                String firstChars = "";
                if (st.length() > 3) {
                    firstChars = st.substring(0, 3);

                }

                if (firstChars.equalsIgnoreCase("img")) {
                    //for the Images

                    st = st.substring(3, st.length() - 1);

                    File file = new File(st);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    imageView.setFitHeight(150);
                    imageView.setFitWidth(200);

                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);

                    if (!cmd.equalsIgnoreCase(lblName.getText())) {

                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        Text text1 = new Text("  " + cmd + " :");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);

                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                        Text text1 = new Text("");
                        hBox.getChildren().add(text1);

                    }
                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));

                } else {

                    TextFlow tempFlow = new TextFlow();

                    if (!cmd.equalsIgnoreCase(lblName.getText() + ":")) {
                        Text txtName = new Text(cmd + " ");
                        txtName.getStyleClass().add("txtName");
                        tempFlow.getChildren().add(txtName);

                        tempFlow.setStyle("-fx-color: rgb(239,242,255);" +
                                "-fx-background-color: rgb(149,208,253);" +
                                " -fx-background-radius: 10px");
                        tempFlow.setPadding(new Insets(3,10,3,10));
                    }

                    tempFlow.getChildren().add(text);
                    tempFlow.setMaxWidth(200); //200

                    TextFlow flow = new TextFlow(tempFlow);

                    HBox hBox = new HBox(12); //12

                    if (!cmd.equalsIgnoreCase(lblName.getText() + ":")) {

                        vBox.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(flow);

                    } else {

                        Text text2 = new Text(fullMsg + " ");
                        TextFlow flow2 = new TextFlow(text2);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                        hBox.setPadding(new Insets(2,5,2,10));

                        flow2.setStyle("-fx-color: rgb(239,242,255);" +
                                "-fx-background-color: rgb(184,248,184);" +
                                "-fx-background-radius: 10px");
                        flow2.setPadding(new Insets(3,10,3,10));
                    }

                    Platform.runLater(() -> vBox.getChildren().addAll(hBox));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btn_logout_On_action(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void txt_massage_on_action(ActionEvent actionEvent) {
        String msg = TxtMassageTypeFeild.getText();
        writer.println(lblName.getText() + ": " + msg);

        TxtMassageTypeFeild.clear();

        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);
        }
    }

    public void imojiOnAction(MouseEvent mouseEvent) {
        imogiPane.setVisible(true);
    }

    public void btn_cam_on_action(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        writer.println(lblName.getText() + " " + "img" + filePath.getPath());
    }

    public void btn_massage_send_on_action(MouseEvent mouseEvent) {
        String msg = TxtMassageTypeFeild.getText();
        writer.println(lblName.getText() + ": " + msg);

        TxtMassageTypeFeild.clear();

        if(msg.equalsIgnoreCase("BYE") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);

        }
    }

    public void sad(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128546));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void lot_sad(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128554));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void money(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(129297));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void love(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128525));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);

    }

    public void green_sad(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128560));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void smile_one_eyy(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128540));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void cry_yes(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128546));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void cry_head(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128550));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void real_amile(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128514));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void tuin(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128519));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void woow(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128559));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void smile_normal(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128513));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void large_smile(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128522));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void small_smile(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128578));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void tong_smile(MouseEvent mouseEvent) {
        String emoji = new String(Character.toChars(128539));
        TxtMassageTypeFeild.setText(emoji);
        imogiPane.setVisible(false);
    }

    public void Mouse_On_Click_Hide_Emogi(MouseEvent mouseEvent) {
        imogiPane.setVisible(false);
    }
}
