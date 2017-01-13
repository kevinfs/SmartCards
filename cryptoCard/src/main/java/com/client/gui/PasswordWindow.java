package com.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import com.github.sarxos.webcam.Webcam;
import model.RGBHistogram;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.client.controller.HttpRequestBuilder;
import com.iris.service.Tools;
import service.CHistogramCalculator;
import service.HistogramFileReader;
import service.HistogramSimilarity;
import service.ImageConverter;

public class PasswordWindow {

    private static HttpRequestBuilder http;
    public JFrame frame;
    private JTextField passwordField;
    Tools tools = new Tools();

    String url;
    String login;
    String graine;
    String sel;
    String numberToSign;
    MultiValueMap<String, String> requestData;

    public void takePicture() throws IOException {
        String fileName = "loginPicture";
        String pngPicture = fileName + ".png";
        //get default webcam and open it
        Webcam webcam = Webcam.getDefault();
        // get image
        Dimension dimension = new Dimension(640, 480);
        webcam.setViewSize(dimension);
        webcam.open();

        // get picture form webcam
        BufferedImage image = webcam.getImage();

        // save image to PNG file
        ImageIO.write(image, "PNG", new File(pngPicture));

        // Image converter to convert from jpeg to ppm
        ImageConverter imConverter = new ImageConverter();
        try {
            imConverter.convert(pngPicture, "ppm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Succesfully converted file!");

        CHistogramCalculator.getHistogramValue();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        String baseURL = "https://localhost:8088";

        try {
            http = new HttpRequestBuilder(baseURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> {
            try {
                PasswordWindow window = new PasswordWindow("login", "graine42", "sel42", "number42");
                window.frame.setTitle("SmartCards");
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PasswordWindow(String login, String graine, String sel, String numberToSign) {
        String baseURL = "https://localhost:8088";

        try {
            http = new HttpRequestBuilder(baseURL);
        } catch (CertificateException | KeyManagementException | IOException | NoSuchAlgorithmException
                | KeyStoreException e) {
            e.printStackTrace();
        }

        initialize();
        this.login = login;
        this.graine = graine;
        this.sel = sel;
        this.numberToSign = numberToSign;
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(200, 200, 950, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(80, 50, 800, 800);
        frame.getContentPane().add(panel);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        panel.add(splitPane);

        JPanel panel_1 = new JPanel();
        splitPane.setLeftComponent(panel_1);

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3);
        panel_3.setLayout(new GridLayout(1, 1, 0, 0));

        JLabel lblPassword = new JLabel("Password ");
        panel_3.add(lblPassword);

        passwordField = new JPasswordField();
        panel_3.add(passwordField);
        passwordField.setColumns(10);
        passwordField.setEnabled(false);

        JPanel panel_2 = new JPanel();
        splitPane.setRightComponent(panel_2);
        GridLayout gbl_panel_2 = new GridLayout(2, 5);
        panel_2.setLayout(gbl_panel_2);

        JLabel status = new JLabel();
        panel_1.add(status);

        int[] solutionArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        shuffleArray(solutionArray);

        for (int i = 0; i < 10; i++) {
            JButton btnNewButton = new JButton(String.valueOf(solutionArray[i]));
            btnNewButton.setPreferredSize(new Dimension(2, 0));
            panel_2.add(btnNewButton);
            btnNewButton.addActionListener(e -> {
                passwordField.setText(passwordField.getText() + btnNewButton.getText());
            });
        }

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            passwordField.setText("");
        });
        panel_2.add(clearButton);

        JButton send = new JButton("Send");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.anchor = GridBagConstraints.SOUTHEAST;
        gbc_btnNewButton.gridx = 0;
        gbc_btnNewButton.gridy = 0;
        panel_1.add(send, gbc_btnNewButton);


        JButton btnNewButton = new JButton("Take picture");
        GridBagConstraints gbc_btnNewButton2 = new GridBagConstraints();
        gbc_btnNewButton2.anchor = GridBagConstraints.SOUTHEAST;
        gbc_btnNewButton2.gridx = 0;
        gbc_btnNewButton2.gridy = 0;
        panel_2.add(btnNewButton, gbc_btnNewButton2);


        btnNewButton.addActionListener(e -> {
            try {
                takePicture();
                status.setText("Picture OK");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


        send.addActionListener(e -> {
            url = "/password";
            String v1 = tools.md5(passwordField.getText() + sel);
            String v2 = tools.md5(v1 + graine);

            requestData = new LinkedMultiValueMap<>();
            requestData.add("login", login);
            requestData.add("password", v2);
            requestData.add("graine", graine);

            String response = http.post(url, requestData);

            if (response.equals("OK")) {
                final double simil = simil();
                if (simil < 80) {
                    response = "KO";
                    status.setText(response);
                    status.setForeground(Color.RED);
                }else {
                    status.setForeground(Color.GREEN);
                    status.setText(response);
                }

            } else {
                status.setForeground(Color.RED);
                status.setText(response);
            }

            System.out.println("response = " + response);
        });
    }

    // Implementing Fisher–Yates shuffle
    static void shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public double simil() {

        double similarity;
        RGBHistogram rgbHistogram;
        RGBHistogram rgbTemporaryHistogram;

        HistogramFileReader refrencehistogramFile = new HistogramFileReader("/Users/glist/codespace/SmartCards/imageProcessing/javaIrisProcess/smartCard/src/main/resources/archives/historef.txt");
        HistogramFileReader temporaryhistogramFile = new HistogramFileReader("/Users/glist/codespace/SmartCards/imageProcessing/javaIrisProcess/smartCard/src/main/resources/archives/histoSH.txt");

        rgbHistogram = refrencehistogramFile.getHistogramme();
        double sumPixels = rgbHistogram.getHistogramTotalPixels();

        rgbTemporaryHistogram = temporaryhistogramFile.getHistogramme();
        HistogramSimilarity histogramSimilarity = new HistogramSimilarity(rgbHistogram, rgbTemporaryHistogram);
        similarity = histogramSimilarity.distance();


        System.out.println("The rate of similarity  :" + similarity);
        return similarity;

    }

}
