package com.client.gui;

import com.client.controller.HttpRequestBuilder;
import com.iris.service.Tools;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.swing.*;
import java.awt.*;

public class PasswordWindow {

    private static HttpRequestBuilder http;
    public JFrame frame;
    private JTextField loginField;
    private JTextField passwordField;
    Tools tools = new Tools();

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

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PasswordWindow window = new PasswordWindow();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public PasswordWindow() {
        initialize();
//        url = "/graine";
//        graine = http.get(url);
//        System.out.println("graine = " + graine);

    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(100, 50, 800, 800);
        frame.getContentPane().add(panel);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        panel.add(splitPane);

        JPanel panel_1 = new JPanel();
        splitPane.setLeftComponent(panel_1);

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3);
        panel_3.setLayout(new GridLayout(0, 1, 0, 0));

        JLabel lblPassword = new JLabel("Password");
        panel_3.add(lblPassword);

        passwordField = new JTextField();
        panel_3.add(passwordField);
        passwordField.setColumns(5);
        passwordField.setEnabled(false);

        JPanel panel_2 = new JPanel();
        splitPane.setRightComponent(panel_2);
        GridLayout gbl_panel_2 = new GridLayout(2,5);
        panel_2.setLayout(gbl_panel_2);

        for (int i = 0; i < 10; i++) {
            JButton btnNewButton = new JButton(String.valueOf(i));
            btnNewButton.setPreferredSize(new Dimension(2,0));
            panel_2.add(btnNewButton);

        }
        JButton btnNewButton = new JButton("New button");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.anchor = GridBagConstraints.SOUTHEAST;
        gbc_btnNewButton.gridx = 0;
        gbc_btnNewButton.gridy = 0;
        panel_2.add(btnNewButton, gbc_btnNewButton);

        btnNewButton.addActionListener(e -> {
            System.out.println("coucou");
//            url = "/login";
//            requestData = new LinkedMultiValueMap<>();
//            requestData.add("login", loginField.getText());
//            requestData.add("password", passwordField.getText());
//            requestData.add("graine", graine);
//
//            String sel = http.post(url, requestData);
//            System.out.println("sel = " + sel);

        });
    }
}
