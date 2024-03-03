package CurrencyConverter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/currencyconverter2")
public class CurrencyConverter2 extends HttpServlet {

    private ArrayList<String> currencyCodes = new ArrayList<>();
    private HashMap<String, String> currencyNames = new HashMap<>();
    private HashMap<String, Double> euroRates = new HashMap<>();

    @Override
    public void init() throws ServletException {
        super.init();
        loadDataFromFile();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Currency Converter</title></head><body>");
        out.println("<h1>Μετατροπή Ποσών σε Διαδοχικά Νομίσματα</h1>");

        String amountStr = request.getParameter("amount");
        String fromCurrency = request.getParameter("from");
        String toCurrency = request.getParameter("to");

        out.println("<form method=\"get\">");
        out.println("Ποσό: <input type=\"text\" name=\"amount\" value=\"" + (amountStr != null ? amountStr : "") + "\">");
        out.println("Απο: ");
        out.println("<select name=\"from\">");
        for (String code : currencyCodes) {
            out.println("<option value=\"" + code + "\"" + (fromCurrency != null && fromCurrency.equals(code) ? " selected" : "") + ">" + currencyNames.get(code) + "</option>");
        }
        out.println("</select>");
        out.println("Σε: ");
        out.println("<select name=\"to\">");
        for (String code : currencyCodes) {
            out.println("<option value=\"" + code + "\"" + (toCurrency != null && toCurrency.equals(code) ? " selected" : "") + ">" + currencyNames.get(code) + "</option>");
        }
        out.println("</select>");
        out.println("<input type=\"submit\" value=\"Convert\">");
        out.println("</form>");

        if (amountStr != null && isValidNumber(amountStr) && fromCurrency != null && toCurrency != null) {
            double amount = Double.parseDouble(amountStr);
            double result = convertCurrency(amount, fromCurrency, toCurrency);
            out.println("<h1>" + amount + " " + currencyNames.get(fromCurrency) + " = " + result + " " + currencyNames.get(toCurrency) + "</h1>");
        } else if (amountStr != null || fromCurrency != null || toCurrency != null) {
            out.println("<h1 style=\"color:red;\">Παρακαλώ εισάγετε έγκυρα δεδομένα.</h1>");
        }

        out.println("</body></html>");
    }

    private boolean isValidNumber(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void loadDataFromFile() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getServletContext().getRealPath("/WEB-INF/euro_rates.txt")), "UTF-8"));

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    double rate = Double.parseDouble(parts[2].trim());

                    currencyCodes.add(code);
                    currencyNames.put(code, name);
                    euroRates.put(code, rate);
                }
            }

            br.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double result;

        if (fromCurrency.equals(toCurrency)) {
            result = amount;
        } else {
            double fromRate = euroRates.get(fromCurrency);
            double toRate = euroRates.get(toCurrency);

            if (fromCurrency.equals("EUR")) {
                result = amount * toRate / 100;
            } else if (toCurrency.equals("EUR")) {
                result = amount / fromRate / 100;
            } else {
                result = (amount / fromRate) * toRate / 100;
            }
        }

        return result;
    }
}
