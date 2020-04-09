package com.example.criticalvalue;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    String[][] table;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int R = 31;
        int C = 10;
        table = new String[R][C];
        int i = -1;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {
            InputStream is = (InputStream) getResources().openRawResource(getResources().getIdentifier("normaldisttable" /* Name of csv file in raw directory */,"raw",getPackageName()));
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {

                i += 1;

                // use comma as separator
                String[] row = line.split(cvsSplitBy);
                table[i] = row;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAGGG","FileNot Found", e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAGGG","IO exception", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private double getZValueAt(double xValue) throws Exception {

        if(xValue == 0){
            return 0.5;
        } else if(xValue > 0){
            int xRound = (int) Math.round(xValue * 1000);
            String XRound = String.valueOf(xRound);
            String subRow = XRound.substring(XRound.length() -2 , XRound.length() -1);
            String subColumn = XRound.substring(0 , XRound.length()-2);

            int columnIndex = Integer.parseInt(subRow);
            int rowIndex = Integer.parseInt(subColumn);

            if(rowIndex < table.length) {
                return Double.parseDouble(table[rowIndex][columnIndex]);
            }else{
                throw new Exception("x value is greater than max value provided in normal distribution table");
            }
        }else {
            int xRound = (int) (-1 * Math.round(xValue * 1000));

            String XRound = String.valueOf(xRound);
            String subRow = XRound.substring(XRound.length() -2, XRound.length() -1);
            String subColumn = XRound.substring(0 , XRound.length()-2);

            int columnIndex = Integer.parseInt(subRow);
            int rowIndex = Integer.parseInt(subColumn);
            if(rowIndex < table.length) {
                return (1 - Double.parseDouble(table[rowIndex][columnIndex]));
            }else{
                throw new Exception("x value is less than min value provided in normal distribution table");
            }
        }
    }
}