package Controllers;

import Models.*;
import Analyzers.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by dimav on 10/6/2016.
 */
public class Controller implements Initializable {

    @FXML
    private Button openButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button runButton;

    @FXML
    private Button polizButton;
    @FXML
    private Button polizButton1;

    @FXML
    private TextArea textarea;

    @FXML
    private TextArea polizArea;
    @FXML
    private TextArea polizArea1;

    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text text3;

    @FXML
    private TableView<Lexeme> table1;

    @FXML
    private TableColumn<Lexeme, Integer> column11;
    @FXML
    private TableColumn<Lexeme, Integer> column12;
    @FXML
    private TableColumn<Lexeme, Integer> column13;
    @FXML
    private TableColumn<Lexeme, Integer> column14;
    @FXML
    private TableColumn<Lexeme, Integer> column15;

    @FXML
    private TableView<Identificators> table2;
    @FXML
    private TableColumn<Identificators, Integer> column21;
    @FXML
    private TableColumn<Identificators, String> column22;
    @FXML
    private TableColumn<Identificators, String> column23;


    @FXML
    private TableView<Constants> table3;
    @FXML
    private TableColumn<Constants, Integer> column31;
    @FXML
    private TableColumn<Constants, Double> column32;

    @FXML
    private TableView<PDALexeme> tableSynt;
    @FXML
    private TableColumn<PDALexeme, Integer> columnLine;
    @FXML
    private TableColumn<PDALexeme, String> columnSynt;
    @FXML
    private TableColumn<PDALexeme, Integer> columnState;
    @FXML
    private TableColumn<PDALexeme, String> columnStack;

    @FXML
    private TableView<PDARules> tablePDA;
    @FXML
    private TableColumn<PDARules, Integer> alpha;
    @FXML
    private TableColumn<PDARules, String> metka;
    @FXML
    private TableColumn<PDARules, Integer> beta;
    @FXML
    private TableColumn<PDARules, Integer> stack;
    @FXML
    private TableColumn<PDARules, String> semant;
    @FXML
    private TableColumn<PDARules, String> semantEqual;

    @FXML
    private TableView<PolizProcessing> polizProcess;
    @FXML
    private TableColumn<PolizProcessing, String> inputPoliz;
    @FXML
    private TableColumn<PolizProcessing, String> stackPoliz;
    @FXML
    private TableColumn<PolizProcessing, String> outputPoliz;

    @FXML
    private TableView<PolizExecution> polizProcess1;
    @FXML
    private TableColumn<PolizExecution, String> stackPoliz1;
    @FXML
    private TableColumn<PolizExecution, String> outputPoliz1;

    private LexicalAnalyzer analyzer;
    private ReversePolishNotationGenerator reversePolishNotationGenerator;


    private ObservableList<Lexeme> userData = FXCollections.observableArrayList();
    private ObservableList<Identificators> userData1 = FXCollections.observableArrayList();
    private ObservableList<Constants> userData2 = FXCollections.observableArrayList();
    private ObservableList<PDALexeme> userData3 = FXCollections.observableArrayList();
    private ObservableList<PDARules> userData4 = FXCollections.observableArrayList();
    private ObservableList<PolizProcessing> userData5 = FXCollections.observableArrayList();
    private ObservableList<PolizExecution> userData6 = FXCollections.observableArrayList();

    private void initData1(LexicalAnalyzer analyzer) {
        ArrayList<Lexeme> listLexeme = analyzer.getLexemes();
        ArrayList<Double> listConstants = analyzer.getConstants();
        ArrayList<String> listIds = analyzer.getIds();
        int i = 1;
        for (Lexeme lexeme : listLexeme) {
            lexeme.setCounter(i);
            if (lexeme.isConstant()) {
                lexeme.setLexName(listConstants.get(lexeme.getCon()).toString());
                lexeme.setLexCode(lexeme.getCon() + 1);
            } else if (lexeme.isId()) {
                lexeme.setLexName(listIds.get(lexeme.getId()));
                lexeme.setLexCode(lexeme.getId() + 1);
            } else {
                lexeme.setLexName(LexicalAnalyzer.lexemesArray[lexeme.getLex()]);
            }
            userData.add(lexeme);
            i++;
        }
    }

    private void initData2(ArrayList<Identificators> arrayList) {
        for (Identificators id : arrayList) {
            userData1.add(new Identificators(id.getCode(), id.getType(), id.getLex()));
        }
    }

    private void initData3(ArrayList<Constants> arrayList) {
        for (Constants id : arrayList) {
            userData2.add(new Constants(id.getCode(), id.getLex()));
        }
    }

    private void initData4(ArrayList<PDALexeme> arrayList) {
        for (PDALexeme id : arrayList) {
            userData3.add(new PDALexeme(id.getNumber() + 1, id.getLexeme(), id.getState(), id.getStack()));
        }
    }

    private void initData5() {
        userData4.add(new PDARules(1, "program", 2, null, "[!=]error", "", 1, null, null));
        userData4.add(new PDARules(2, "IDN", 3, null, "[!=]error", "", 37, null, null));
        userData4.add(new PDARules(3, "var", 4, null, "[!=]error", "", 2, null, null));
        userData4.add(new PDARules(4, "int", 5, null, "[!=]error", "", 3, null, null));
        userData4.add(new PDARules(4, "real", 5, null, "[!=]error", "", 4, null, null));
        userData4.add(new PDARules(5, "IDN", 6, null, "[!=]error", "", 37, null, null));
        userData4.add(new PDARules(6, ",", 5, null, "[!=]error", "", 21, null, null));
        userData4.add(new PDARules(6, "¶", 4, null, "[!=]error", "", 20, null, null));
        userData4.add(new PDARules(6, "{", 101, 7, "[!=]error", "", 15, null, null));
        userData4.add(new PDARules(7, "¶", 8, null, "[!=]error", "", 20, null, null));
        userData4.add(new PDARules(8, "}", null, null, "[!=]п/а опер 101 ↓7", "[=]вихід", 16, 101, 7));

        userData4.add(new PDARules(101, "Write", 102, null, "[!=]error", "", 6, null, null));
        userData4.add(new PDARules(101, "Read", 102, null, "[!=]error", "", 5, null, null));
        userData4.add(new PDARules(101, "for", 105, null, "[!=]error", "", 7, null, null));
        userData4.add(new PDARules(101, "if", 301, 111, "[!=]error", "", 12, null, null));
        userData4.add(new PDARules(101, "IDN", 114, null, "[!=]error", "", 37, null, null));
        userData4.add(new PDARules(102, "(", 103, null, "[!=]error", "", 28, null, null));
        userData4.add(new PDARules(103, "IDN", 104, null, "[!=]error", "", 37, null, null));
        userData4.add(new PDARules(104, ",", 103, null, "[!=]error", "", 21, null, null));
        userData4.add(new PDARules(104, ")", null, null, "[!=]error", "вихід", 29, null, null));
        userData4.add(new PDARules(105, "IDN", 106, null, "[!=]error", "", 37, null, null));
        userData4.add(new PDARules(106, "=", 201, 107, "[!=]error", "", 30, null, null));
        userData4.add(new PDARules(107, "by", 201, 108, "[!=]error", "", 8, null, null));
        userData4.add(new PDARules(108, "to", 201, 109, "[!=]error", "", 9, null, null));
        userData4.add(new PDARules(109, "do", 101, 110, "[!=]error", "", 10, null, null));
        userData4.add(new PDARules(110, "rof", null, null, "[!=]error", "вихід", 11, null, null));
        userData4.add(new PDARules(111, "then", 101, 112, "[!=]error", "", 13, null, null));
        userData4.add(new PDARules(112, "¶", 113, null, "[!=]error", "", 20, null, null));
        userData4.add(new PDARules(113, "fi", null, null, "[!=]п/а опер 101 ↓112", "вихід", 14, 101, 112));
        userData4.add(new PDARules(114, "=", 115, null, "[!=]error", "", 30, null, null));
        userData4.add(new PDARules(115, "{", 301, 116, "[!=]error", "", 15, 201, 119));
        userData4.add(new PDARules(116, "?", 201, 117, "[!=]error", "", 22, null, null));
        userData4.add(new PDARules(117, ":", 201, 118, "[!=]error", "вихід", 23, null, null));
        userData4.add(new PDARules(118, "}", null, null, "[!=]error", "", 16, null, null));
        userData4.add(new PDARules(119, "any lexeme", null, null, "вихід", "", null, -1, null));

        userData4.add(new PDARules(201, "IDN", 202, null, "[!=]error", "", 37, null, null));
        userData4.add(new PDARules(201, "CON", 202, null, "[!=]error", "", 38, null, null));
        userData4.add(new PDARules(201, "(", 201, 203, "[!=]error", "", 28, null, null));
        userData4.add(new PDARules(202, "+", 201, null, "вихід", "", 24, -1, null));
        userData4.add(new PDARules(202, "-", 201, null, "вихід", "", 25, -1, null));
        userData4.add(new PDARules(202, "*", 201, null, "вихід", "", 26, -1, null));
        userData4.add(new PDARules(202, "/", 201, null, "вихід", "", 27, -1, null));
        userData4.add(new PDARules(203, ")", 202, null, "[!=]error", "", 29, null, null));

        userData4.add(new PDARules(301, "not", 301, null, "[!=]п/а вираз 201 ↓302", "", 19, 201, 302));
        userData4.add(new PDARules(301, "(", 201, 304, "[!=]п/а вираз 201 ↓302", "", 19, 201, 302));
        userData4.add(new PDARules(302, "<", 201, 303, "[!=]error", "", 31, null, null));
        userData4.add(new PDARules(302, ">", 201, 303, "[!=]error", "", 32, null, null));
        userData4.add(new PDARules(302, "!=", 201, 303, "[!=]error", "", 36, null, null));
        userData4.add(new PDARules(302, "==", 201, 303, "[!=]error", "", 35, null, null));
        userData4.add(new PDARules(302, "<=", 201, 303, "[!=]error", "", 33, null, null));
        userData4.add(new PDARules(302, ">=", 201, 303, "[!=]error", "", 34, null, null));
        userData4.add(new PDARules(303, "and", 301, null, "[!=]вихід", "вихід", 18, -1, null));
        userData4.add(new PDARules(303, "or", 301, null, "[!=]вихід", "вихід", 17, -1, null));
        userData4.add(new PDARules(304, ")", 303, null, "[!=]error", "", 29, null, null));
    }

    private void initData6(ArrayList<PolizProcessing> poliz) {
        for (PolizProcessing id : poliz) {
            userData5.add(new PolizProcessing(id.getInputPoliz(), id.getStackPoliz(), id.getOutputPoliz()));
        }
    }

    private void initData7(ArrayList<PolizExecution> poliz) {
        for (PolizExecution id : poliz) {
            userData6.add(new PolizExecution(id.getStack(), id.getPoliz()));
        }
    }

    private void setCells() {
        column11.setCellValueFactory(new PropertyValueFactory<>("counter"));
        column12.setCellValueFactory(new PropertyValueFactory<>("line"));
        column13.setCellValueFactory(new PropertyValueFactory<>("lexName"));
        column14.setCellValueFactory(new PropertyValueFactory<>("lex"));
        column15.setCellValueFactory(new PropertyValueFactory<>("lexCode"));
        column21.setCellValueFactory(new PropertyValueFactory<>("code"));
        column22.setCellValueFactory(new PropertyValueFactory<>("lex"));
        column23.setCellValueFactory(new PropertyValueFactory<>("type"));
        column31.setCellValueFactory(new PropertyValueFactory<>("code"));
        column32.setCellValueFactory(new PropertyValueFactory<>("lex"));
        columnLine.setCellValueFactory(new PropertyValueFactory<>("number"));
        columnSynt.setCellValueFactory(new PropertyValueFactory<>("lexeme"));
        columnState.setCellValueFactory(new PropertyValueFactory<>("state"));
        columnStack.setCellValueFactory(new PropertyValueFactory<>("stack"));
        alpha.setCellValueFactory(new PropertyValueFactory<>("alpha"));
        metka.setCellValueFactory(new PropertyValueFactory<>("metka"));
        beta.setCellValueFactory(new PropertyValueFactory<>("beta"));
        stack.setCellValueFactory(new PropertyValueFactory<>("stack"));
        semant.setCellValueFactory(new PropertyValueFactory<>("semant"));
        semantEqual.setCellValueFactory(new PropertyValueFactory<>("semantEqual"));
        inputPoliz.setCellValueFactory(new PropertyValueFactory<>("inputPoliz"));
        stackPoliz.setCellValueFactory(new PropertyValueFactory<>("stackPoliz"));
        outputPoliz.setCellValueFactory(new PropertyValueFactory<>("outputPoliz"));
        stackPoliz1.setCellValueFactory(new PropertyValueFactory<>("stack"));
        outputPoliz1.setCellValueFactory(new PropertyValueFactory<>("poliz"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCells();
        initData5();
        tablePDA.setItems(userData4);
        openButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialDirectory(new File("C:\\Users\\dimav\\Documents\\Translators\\src"));
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
                );
                File selectedFile = fileChooser.showOpenDialog(null);
                String file = null;
                try (InputStream stream = new FileInputStream(selectedFile)) {
                    byte[] b = new byte[stream.available()];
                    stream.read(b);
                    file = new String(b);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textarea.setText(file);
            }
        });
        runButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                table1.getItems().clear();
                table2.getItems().clear();
                table3.getItems().clear();
                tableSynt.getItems().clear();
                text1.setText("Line for exceptions in Lexical Analyzer");
                text2.setText("Line for exceptions in Syntax Analyzer");
                text3.setText("Line for exceptions in POLIZ");
                String file = textarea.getText();
                try {
                    analyzer = new LexicalAnalyzer(file);
                    try {
                        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(analyzer);
                        initData4(syntaxAnalyzer.getPdaLexemes());
                    } catch (Exception e) {
                        text2.setText(e.getMessage());
                    }
                    initData1(analyzer);
                    initData2(analyzer.getIdentificators());
                    initData3(analyzer.getConstantses());
                } catch (Exception e) {
                    text1.setText(e.getMessage());
                }
                table1.setItems(userData);
                table2.setItems(userData1);
                table3.setItems(userData2);
                tableSynt.setItems(userData3);
            }
        });
        polizButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                polizProcess.getItems().clear();
                reversePolishNotationGenerator = new ReversePolishNotationGenerator(analyzer.getLexemes());
                polizArea.setText(reversePolishNotationGenerator.generatePoliz());
                initData6(reversePolishNotationGenerator.getPolizProcessing());
                polizProcess.setItems(userData5);
            }
        });
        polizButton1.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                polizProcess1.getItems().clear();
                ReversePolishNotationExecution reversePolishNotationExecution = new ReversePolishNotationExecution(reversePolishNotationGenerator);
                try {
                    reversePolishNotationExecution.runProgram();
                    initData7(reversePolishNotationExecution.getPolizExecutions());
                    polizProcess1.setItems(userData6);
                } catch (Exception e) {
                    text3.setText(e.getMessage());
                    e.printStackTrace();
                }
                polizArea1.setText(reversePolishNotationExecution.getSolvedPoliz());
            }
        });
        saveButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                String file = textarea.getText();
                FileChooser fileChooser = new FileChooser();
//                fileChooser.setInitialDirectory(new File("C:\\Users\\dimav\\Translators"));
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt")
                );
                File selectedFile = fileChooser.showSaveDialog(null);
                if (selectedFile != null) {
                    try {
                        FileWriter fileWriter = new FileWriter(selectedFile);
                        fileWriter.write(file);
                        fileWriter.close();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });
    }
}
