package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SerializeApplication extends Application {

    final static String DEFAULT = "Default";
    final static String CUSTOM = "Custom";
    final static String EXTERNALIZE = "With externalize";
    final static String METHODS = "With methods";

    @Override
    public void start(Stage stage) {
        Label lbl = new Label();
        TextArea textArea = new TextArea();
        textArea.setPrefColumnCount(10);
        textArea.setPrefRowCount(1);

        final CategoryAxis x = new CategoryAxis();
        final NumberAxis y = new NumberAxis();
        final CategoryAxis x1 = new CategoryAxis();
        final NumberAxis y1 = new NumberAxis();
        final BarChart<String, Number> timeHistogram = new BarChart<>(x, y);
        final BarChart<String, Number> memoryHistogram = new BarChart<>(x1, y1);
        timeHistogram.setTitle("Serialization and Deserialization time");
        memoryHistogram.setTitle("Memory");

        x.setLabel("Type of serialization");
        y.setLabel("time, ms");

        x1.setLabel("Type of serialization");
        y1.setLabel("memory, bytes");

        // инициализация с нулевыми параметрами - хотим при запуске отобразить пустые гистограммы
        XYChart.Series<String, Number> s1 = new XYChart.Series<>();
        s1.setName("Serialization");
        s1.getData().addAll(new XYChart.Data<>(DEFAULT, 0), new XYChart.Data<>(CUSTOM, 0),
                new XYChart.Data<>(EXTERNALIZE, 0), new XYChart.Data<>(METHODS, 0));

        XYChart.Series<String, Number> s2 = new XYChart.Series<>();
        s2.setName("Deserialization");
        s2.getData().addAll(new XYChart.Data<>(DEFAULT, 0), new XYChart.Data<>(CUSTOM, 0),
                new XYChart.Data<>(EXTERNALIZE, 0), new XYChart.Data<>(METHODS, 0));


        XYChart.Series<String, Number> s11 = new XYChart.Series<>();
        s11.setName("Size of serialized file");
        s11.getData().addAll(new XYChart.Data<>(DEFAULT, 0), new XYChart.Data<>(CUSTOM, 0),
                new XYChart.Data<>(EXTERNALIZE, 0), new XYChart.Data<>(METHODS, 0));

        timeHistogram.getData().addAll(s1, s2);
        memoryHistogram.getData().add(s11);

        // кнопка для отправки данных, которые ввел пользователь в поле
        Button btn = new Button("Send");
        lbl.setText("Enter the number of objects to be serialized:");

        btn.setOnAction(event -> {
            try {
                SerializerTest serializerTest = new SerializerTest(Integer.parseInt(textArea.getText()));
                // запуск тестов
                serializerTest.setUp();
                serializerTest.testDefaultSerialize();
                serializerTest.testCustomSerialize();
                serializerTest.testSerializeWithExternalize();
                serializerTest.testSerializeWithMethods();
                serializerTest.tearDown();

                // добавляем значения, которые получили в результатет тестирования
                s1.getData().addAll(new XYChart.Data(DEFAULT, serializerTest.defaultSerializeTime), new XYChart.Data(CUSTOM, serializerTest.customSerializeTime),
                        new XYChart.Data(EXTERNALIZE, serializerTest.externalSerializeTime), new XYChart.Data(METHODS, serializerTest.methodsSerializeTime));


                s2.getData().addAll(new XYChart.Data(DEFAULT, serializerTest.defaultDeserializeTime), new XYChart.Data(CUSTOM, serializerTest.customDeserializeTime),
                        new XYChart.Data(EXTERNALIZE, serializerTest.externalDeserializeTime), new XYChart.Data(METHODS, serializerTest.methodsDeserializeTime));


                s11.getData().addAll(new XYChart.Data(DEFAULT, serializerTest.defaultMemory), new XYChart.Data(CUSTOM, serializerTest.customMemory),
                        new XYChart.Data(EXTERNALIZE, serializerTest.externalMemory), new XYChart.Data(METHODS, serializerTest.methodsMemory));

                timeHistogram.getData().addAll(s1, s2);
                memoryHistogram.getData().add(s11);

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        VBox vbox = new VBox(timeHistogram);
        VBox vbox1 = new VBox(memoryHistogram);
        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10, lbl, textArea, btn, vbox, vbox1);
        root.setAlignment(Pos.BOTTOM_CENTER);
        Scene scene = new Scene(root, 1200, 600);
        stage.setTitle("Serialization of object");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}