package com.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.client.controller.HttpRequestBuilder;
import com.client.smartcard.CardCommandHelper;
import com.client.smartcard.CardUtils;
import com.client.smartcard.TestSmartCard;
import com.iris.service.Tools;

public class PasswordWindow {

	private static HttpRequestBuilder http;
	public JFrame frame;
	private JTextField passwordField;
	Tools tools = new Tools();

	private static CardTerminal cardTerminal;
	private static Card card;
	private static CardChannel cardChannel;
	private static CardCommandHelper cardCommandHelper;

	String url;
	String login;
	String graine;
	String sel;
	String numberToSign;
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

		int[] solutionArray = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
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
		panel_2.add(send, gbc_btnNewButton);

		send.addActionListener(e -> {
			url = "/password";
			String v1 = tools.md5(passwordField.getText() + sel);
			String v2 = tools.md5(v1 + graine);
			String card = "";

			try {
				card = getSignatureFromCard();
			} catch (CardException e2) {
			}

			requestData = new LinkedMultiValueMap<>();
			requestData.add("login", login);
			requestData.add("password", v2);
			requestData.add("graine", graine);
			requestData.add("card", card);

			String response = http.post(url, requestData);
			status.setText(response);
			if (response.equals("OK")) {
				status.setForeground(Color.GREEN);
			} else {
				status.setForeground(Color.RED);
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

	public String getSignatureFromCard() throws CardException {

		connectToTerminal();

		cardTerminal.waitForCardPresent(4000);
		if (cardTerminal.isCardPresent()) {

			connectToCard();

			try {

				Key privateKey = cardCommandHelper.retrieveSecretKey();

				// Sign
				Signature ecdsaSign = Signature.getInstance("SHA1withECDSA", "SunEC");
				ecdsaSign.initSign((PrivateKey) privateKey);
				ecdsaSign.update(numberToSign.getBytes());
				byte[] signature = ecdsaSign.sign();

				String signEncoded = Base64.getEncoder().encodeToString(signature);

				return signEncoded;

			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			card.disconnect(true);

		}

		return "";
	}

	public static List<CardTerminal> getTerminals() throws CardException {
		return TerminalFactory.getDefault().terminals().list();
	}

	public static void connectToTerminal() {
		List<CardTerminal> terminauxDispos;
		try {
			terminauxDispos = TestSmartCard.getTerminals();
			if (terminauxDispos.size() > 0) {
				cardTerminal = terminauxDispos.get(0);
				System.out.println("Connected to : " + cardTerminal.toString());
			}
		} catch (CardException e) {
			System.err.println("Unable to find a terminal");
		}
	}

	public static void connectToCard() {
		try {
			// Card connection
			card = cardTerminal.connect("T=0");

			// ATR (answer To Reset)
			System.out.println("Connected to : " + CardUtils.decodeToBytesForUser(card.getATR().getBytes()));

			// Open channel
			cardChannel = card.getBasicChannel();
			cardCommandHelper = new CardCommandHelper(cardChannel);
		} catch (CardException e) {
			System.err.println("Unable to connect to a card");
		}
	}
}
