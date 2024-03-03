

package CurrencyConverter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/currencyconverter")
public class CurrencyConverter extends HttpServlet {
    private static final double EUR_TO_USD = 1.06;
    private static final double EUR_TO_GBP = 0.84;

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
        out.println("<option value=\"Ευρώ\"" + (fromCurrency != null && fromCurrency.equals("Ευρώ") ? " selected" : "") + ">Ευρώ</option>");
        out.println("<option value=\"Δολάρια\"" + (fromCurrency != null && fromCurrency.equals("Δολάρια") ? " selected" : "") + ">Δολάρια</option>");
        out.println("<option value=\"Λίρες\"" + (fromCurrency != null && fromCurrency.equals("Λίρες") ? " selected" : "") + ">Λίρες</option>");
        out.println("</select>");
        out.println("Σε: ");
        out.println("<select name=\"to\">");
        out.println("<option value=\"Ευρώ\"" + (toCurrency != null && toCurrency.equals("Ευρώ") ? " selected" : "") + ">Ευρώ</option>");
        out.println("<option value=\"Δολάρια\"" + (toCurrency != null && toCurrency.equals("Δολάρια") ? " selected" : "") + ">Δολάρια</option>");
        out.println("<option value=\"Λίρες\"" + (toCurrency != null && toCurrency.equals("Λίρες") ? " selected" : "") + ">Λίρες</option>");
        out.println("</select>");
        out.println("<input type=\"submit\" value=\"Convert\">");
        out.println("</form>");

        if (amountStr != null && isValidNumber(amountStr) && fromCurrency != null && toCurrency != null) {
            double amount = Double.parseDouble(amountStr);
            double result = convertCurrency(amount, fromCurrency, toCurrency);
            out.println("<h1>" + amount + " " + fromCurrency + " = " + result + " " + toCurrency + "</h1>");
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

    private double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double result;
        if (fromCurrency.equals("Ευρώ")) {
            if (toCurrency.equals("Δολάρια")) {
                result = amount * EUR_TO_USD;
            } else if (toCurrency.equals("Λίρες")) {
                result = amount * EUR_TO_GBP;
            } else {
                result = amount;
            }
        } else if (fromCurrency.equals("Δολάρια")) {
            if (toCurrency.equals("Ευρώ")) {
                result = amount / EUR_TO_USD;
            } else if (toCurrency.equals("Λίρες")) {
                result = (amount / EUR_TO_USD) * EUR_TO_GBP;
            } else {
                result = amount;
            }
        } else if (fromCurrency.equals("Λίρες")) {
            if (toCurrency.equals("Ευρώ")) {
                result = amount / EUR_TO_GBP;
            } else if (toCurrency.equals("Δολάρια")) {
                result = (amount / EUR_TO_GBP) * EUR_TO_USD;
            } else {
                result = amount;
            }
        } else {
            result = amount;
        }
        return result;
    }
}
