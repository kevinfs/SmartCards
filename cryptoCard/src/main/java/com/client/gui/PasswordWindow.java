package com.client.gui;

import com.client.controller.HttpRequestBuilder;
import com.iris.service.Tools;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PasswordWindow {

    private static HttpRequestBuilder http;
    public JFrame frame;
    private JTextField passwordField;
    Tools tools = new Tools();

    String url;
    String login;
    String graine;
    String sel;
    MultiValueMap<String, String> requestData;

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
                PasswordWindow window = new PasswordWindow("login", "graine42", "sel42");
                window.frame.setTitle("SmartCards");
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PasswordWindow(String login, String graine, String sel) {
        String baseURL = "https://localhost:8088";

        try {
            http = new HttpRequestBuilder(baseURL);
        } catch (CertificateException | KeyManagementException | IOException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }

        initialize();
        this.login = login;
        this.graine = graine;
        this.sel = sel;
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
                    }
            );
        }

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
                    passwordField.setText("");
                }
        );
        panel_2.add(clearButton);

        JButton send = new JButton("Send");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.anchor = GridBagConstraints.SOUTHEAST;
        gbc_btnNewButton.gridx = 0;
        gbc_btnNewButton.gridy = 0;
        panel_2.add(send, gbc_btnNewButton);

        send.addActionListener(e -> {
            url = "/password";
            String v1 = tools.md5(passwordField.getText() + sel);
            String v2 = tools.md5(v1 + graine);

            requestData = new LinkedMultiValueMap<>();
            requestData.add("login", login);
            requestData.add("password", v2);
            requestData.add("graine", graine);

            String response = http.post(url, requestData);
            status.setText(response);
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
}
