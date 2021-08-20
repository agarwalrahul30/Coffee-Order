package com.example.coffeeorder;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;
    String priceMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText name = findViewById(R.id.name);
        String customer = name.getText().toString();

        CheckBox whippedCream = findViewById(R.id.cream);
        boolean creamcheck = whippedCream.isChecked();

        CheckBox choco = findViewById(R.id.chocolate);
        boolean chococheck = choco.isChecked();

        int price = calculatePrice(creamcheck,chococheck);
        priceMessage = orderSummary(customer,price,creamcheck,chococheck);
        displayOrderMessage(priceMessage+"\nThank You!!");

    }

    public void email(View view) {
        composeEmail(priceMessage);
    }

    private void composeEmail(String priceMessage) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee Order from KGA");
        intent.putExtra(Intent.EXTRA_TEXT, "Thank You for ordering from us!\n\n"
                +"Order Summary\n\n"+priceMessage);

        startActivity(Intent.createChooser(intent, "Choose App"));
    }

    public String orderSummary(String customer, int price, boolean creamcheck, boolean chococheck) {
        String summary = "Name: "+customer;
        summary+= "\nQuantity: "+quantity;
        String check = creamcheck?"Yes":"No";
        summary+= "\nAdded Whipped Cream: "+check;
        check = chococheck?"Yes":"No";
        summary+= "\nAdded Chocolate: "+check;
        summary+= "\nTotal: $"+price;
        return summary;
    }

    public void increment(View view) {
        if(quantity<100) {
            quantity++;
            displayQuantity(quantity);
            displayPrice(calculatePrice());
        }
        else
            Toast.makeText(this,"You can order a maximum of 100 coffees only",Toast.LENGTH_SHORT).show();

    }

    public void decrement(View view) {
        if(quantity>0) {
            quantity--;
            displayQuantity(quantity);
            displayPrice(calculatePrice());
        }
        else
            Toast.makeText(this,"Please have something:)",Toast.LENGTH_SHORT).show();
    }

    private int calculatePrice() {
        return quantity*5;
    }

    private int calculatePrice(boolean checkW,boolean checkC) {
        if(checkW && checkC)
            return quantity*(5 + 1 + 2);
        else if(checkW)
            return quantity*(5 + 1);
        else if(checkC)
            return quantity*(5 + 2);
        else
            return quantity*5;
    }

    private void displayQuantity(int number) {
        quantity = number;
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);
    }


    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }


    private void displayOrderMessage(String message) {

        if(quantity > 0) {
            View view = findViewById(R.id.line);
            TextView orderHeading = findViewById(R.id.order_heading);
            TextView orderMessage = (TextView) findViewById(R.id.order_summary_text_view);

            view.setVisibility(View.VISIBLE);
            orderHeading.setVisibility(View.VISIBLE);

            orderMessage.setText(message);
            orderMessage.setVisibility(View.VISIBLE);

            TextView error = findViewById(R.id.error);
            error.setVisibility(View.INVISIBLE);
        }

        else {
            TextView error = findViewById(R.id.error);
            error.setVisibility(View.VISIBLE);
        }
    }

    public void New(View view) {
        EditText name = findViewById(R.id.name);
        name.getText().clear();

        displayQuantity(0);
        displayPrice(0);

        TextView error = findViewById(R.id.error);
        error.setVisibility(View.GONE);

        View line = findViewById(R.id.line);
        TextView orderHeading = findViewById(R.id.order_heading);
        TextView orderMessage = (TextView) findViewById(R.id.order_summary_text_view);

        line.setVisibility(View.INVISIBLE);
        orderHeading.setVisibility(View.INVISIBLE);
        orderMessage.setVisibility(View.INVISIBLE);

        CheckBox checkW = findViewById(R.id.cream);
        checkW.setChecked(false);

        CheckBox checkC = findViewById(R.id.chocolate);
        checkC.setChecked(false);

    }

}
