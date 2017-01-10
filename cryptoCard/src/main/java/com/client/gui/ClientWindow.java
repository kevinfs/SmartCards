package com.client.gui;

import com.client.controller.HttpRequestBuilder;
import com.iris.service.Tools;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.awt.*;

import javax.swing.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class ClientWindow {

    private static HttpRequestBuilder http;
    private JFrame frame;
    private JTextField loginField;

    String url;
    String graine;
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
                ClientWindow window = new ClientWindow();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public ClientWindow() {
        initialize();
        url = "/graine";
        graine = http.get(url);
        System.out.println("graine = " + graine);

    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 320);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(100, 50, 300, 200);
        frame.getContentPane().add(panel);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        panel.add(splitPane);

        JPanel panel_1 = new JPanel();
        splitPane.setLeftComponent(panel_1);

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3);
        panel_3.setLayout(new GridLayout(0, 1, 0, 0));

        JLabel lblLogin = new JLabel("Login");
        panel_3.add(lblLogin);

        loginField = new JTextField();
        panel_3.add(loginField);
        loginField.setColumns(10);

        JPanel panel_2 = new JPanel();
        splitPane.setRightComponent(panel_2);
        GridBagLayout gbl_panel_2 = new GridBagLayout();
        gbl_panel_2.columnWidths = new int[]{117, 0};
        gbl_panel_2.rowHeights = new int[]{29, 0};
        gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
        gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel_2.setLayout(gbl_panel_2);

        JButton btnNewButton = new JButton("Next");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.anchor = GridBagConstraints.SOUTHEAST;
        gbc_btnNewButton.gridx = 0;
        gbc_btnNewButton.gridy = 0;
        panel_2.add(btnNewButton, gbc_btnNewButton);

        btnNewButton.addActionListener(e -> {
            url = "/login";
            requestData = new LinkedMultiValueMap<>();
            requestData.add("login", loginField.getText());

            String sel = http.post(url, requestData);
            System.out.println("sel = " + sel);

            PasswordWindow window = new PasswordWindow(loginField.getText(), graine, sel);
            frame.setVisible(false);
            window.frame.setVisible(true);

        });
    }
}
