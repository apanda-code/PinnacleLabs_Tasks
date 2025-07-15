import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CurrencyConverterUI {

    static HashMap<String, Double> exchangeRates = new HashMap<>();
    static HashMap<String, String> currencySymbols = new HashMap<>();

    static String[] currencies = {
            "USD", "INR", "EUR", "GBP", "AUD",
            "CAD", "JPY", "CNY", "SGD", "NZD",
            "CHF", "HKD", "KRW", "ZAR", "SEK",
            "NOK", "RUB", "BRL", "MXN", "AED"
    };

    public static void main(String[] args) {

        // Hardcoded exchange rates (base: USD)
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("INR", 83.0);
        exchangeRates.put("EUR", 0.91);
        exchangeRates.put("GBP", 0.78);
        exchangeRates.put("AUD", 1.51);
        exchangeRates.put("CAD", 1.36);
        exchangeRates.put("JPY", 157.0);
        exchangeRates.put("CNY", 7.25);
        exchangeRates.put("SGD", 1.35);
        exchangeRates.put("NZD", 1.62);
        exchangeRates.put("CHF", 0.89);
        exchangeRates.put("HKD", 7.84);
        exchangeRates.put("KRW", 1290.0);
        exchangeRates.put("ZAR", 18.0);
        exchangeRates.put("SEK", 10.4);
        exchangeRates.put("NOK", 10.5);
        exchangeRates.put("RUB", 90.0);
        exchangeRates.put("BRL", 5.25);
        exchangeRates.put("MXN", 17.2);
        exchangeRates.put("AED", 3.67);

        // Currency symbols
        currencySymbols.put("USD", "$");
        currencySymbols.put("INR", "₹");
        currencySymbols.put("EUR", "€");
        currencySymbols.put("GBP", "£");
        currencySymbols.put("AUD", "A$");
        currencySymbols.put("CAD", "C$");
        currencySymbols.put("JPY", "¥");
        currencySymbols.put("CNY", "¥");
        currencySymbols.put("SGD", "S$");
        currencySymbols.put("NZD", "NZ$");
        currencySymbols.put("CHF", "CHF");
        currencySymbols.put("HKD", "HK$");
        currencySymbols.put("KRW", "₩");
        currencySymbols.put("ZAR", "R");
        currencySymbols.put("SEK", "kr");
        currencySymbols.put("NOK", "kr");
        currencySymbols.put("RUB", "₽");
        currencySymbols.put("BRL", "R$");
        currencySymbols.put("MXN", "Mex$");
        currencySymbols.put("AED", "د.إ");

        // Frame setup
        JFrame frame = new JFrame("Currency Converter");
        frame.setSize(650, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.decode("#ffe5b4")); // peach background

        // Title
        JLabel title = new JLabel("CURRENCY CONVERTER");
        title.setBounds(130, 20, 500, 40);
        title.setFont(new Font("Georgia", Font.BOLD, 30));
        title.setForeground(new Color(0, 102, 204));
        frame.add(title);

        // Fonts
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font resultFont = new Font("Segoe UI", Font.BOLD, 16);

        // Amount
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(labelFont);
        amountLabel.setBounds(60, 90, 100, 30);
        frame.add(amountLabel);

        JTextField amountField = new JTextField();
        amountField.setBounds(160, 90, 200, 30);
        frame.add(amountField);

        // From currency
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(labelFont);
        fromLabel.setBounds(60, 140, 100, 30);
        frame.add(fromLabel);

        JComboBox<String> fromCombo = new JComboBox<>(currencies);
        fromCombo.setBounds(160, 140, 200, 30);
        fromCombo.setRenderer(new FlagComboBoxRenderer());
        frame.add(fromCombo);

        // To currency
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(labelFont);
        toLabel.setBounds(60, 190, 100, 30);
        frame.add(toLabel);

        JComboBox<String> toCombo = new JComboBox<>(currencies);
        toCombo.setBounds(160, 190, 200, 30);
        toCombo.setRenderer(new FlagComboBoxRenderer());
        frame.add(toCombo);

        // 🔁 Swap Button with up/down arrows
        JButton swapBtn = new JButton("Swap");
        swapBtn.setBounds(380, 160, 120, 30);
        swapBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        swapBtn.setBackground(new Color(255, 204, 0));
        swapBtn.setForeground(Color.BLACK);
        swapBtn.setBorderPainted(false);
        swapBtn.setFocusPainted(false);
        swapBtn.setContentAreaFilled(true);
        frame.add(swapBtn);

        swapBtn.addActionListener(e -> {
            int fromIndex = fromCombo.getSelectedIndex();
            int toIndex = toCombo.getSelectedIndex();
            fromCombo.setSelectedIndex(toIndex);
            toCombo.setSelectedIndex(fromIndex);
        });

        // Convert Button (Blue)
        JButton convertBtn = new JButton("Convert");
        convertBtn.setBounds(230, 250, 150, 40);
        convertBtn.setBackground(new Color(0, 102, 204)); // blue
        convertBtn.setForeground(Color.WHITE);
        convertBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        frame.add(convertBtn);

        // Result Text Field (Green)
        JTextField resultField = new JTextField("Result will appear here...");
        resultField.setBounds(100, 320, 420, 35);
        resultField.setFont(resultFont);
        resultField.setForeground(new Color(0, 153, 0)); // green
        resultField.setHorizontalAlignment(JTextField.CENTER);
        resultField.setEditable(false);
        frame.add(resultField);

        // Convert Logic using hardcoded exchange rates
        convertBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String from = fromCombo.getSelectedItem().toString();
                String to = toCombo.getSelectedItem().toString();

                double usdAmount = amount / exchangeRates.get(from);
                double converted = usdAmount * exchangeRates.get(to);

                String fromSymbol = currencySymbols.getOrDefault(from, "");
                String toSymbol = currencySymbols.getOrDefault(to, "");

                resultField.setText(String.format("%.2f %s (%s) = %.2f %s (%s)",
                        amount, from, fromSymbol, converted, to, toSymbol));
            } catch (NumberFormatException ex) {
                resultField.setText("Please enter a valid amount.");
            } catch (Exception ex) {
                resultField.setText("Conversion error.");
            }
        });

        frame.setVisible(true);
    }

    // Renderer to show flag beside currency
    static class FlagComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String currency = value.toString();
            String iconPath = "resources/" + currency.toLowerCase() + ".png";
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(24, 16, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
            label.setHorizontalTextPosition(SwingConstants.RIGHT);
            return label;
        }
    }
}
