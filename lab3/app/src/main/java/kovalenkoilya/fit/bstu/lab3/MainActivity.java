package kovalenkoilya.fit.bstu.lab3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import kovalenkoilya.fit.bstu.lab3.R;

public class MainActivity extends Activity {
    private final static String NOTES="notes.txt";
    private EditText editor;
    public Button btsave;
    public Button btread;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        editor=(EditText)findViewById(R.id.editor);
        editor.setBackgroundColor(240);
        Button btn=(Button)findViewById(R.id.close);

        btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btread=(Button)findViewById(R.id.bread);

        btread.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                try {
                    InputStream in=openFileInput(NOTES);

                    if (in!=null) {
                        InputStreamReader tmp=new InputStreamReader(in);
                        BufferedReader reader=new BufferedReader(tmp);
                        String str="";
                        StringBuilder buf=new StringBuilder();

                        while ((str = reader.readLine()) != null) {
                            buf.append(str+"\n");
                        }

                        in.close();
                        editor.setText(buf.toString());
                    }
                }
                catch (Exception e) {
                    // that's OK, we probably haven't created it yet

                    try {
                        FileOutputStream mOutput = openFileOutput(NOTES, Activity.MODE_PRIVATE);
                        String data = "THIS DATA WRITTEN TO A FILE FIRST TIME!!!";
                        mOutput.write(data.getBytes());
                        mOutput.close();
                    } catch (FileNotFoundException ea) {
                        ea.printStackTrace();
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }

                }


                //mytext_finished
            }
        });

        btsave=(Button)findViewById(R.id.bsave);

        btsave.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                try {
                    FileOutputStream mOutput = openFileOutput(NOTES, Activity.MODE_PRIVATE);
                    String data = editor.getText().toString();
                    if (!data.matches("^\\w+\\s{1}\\w+$"))
                    {
                        showDialog("incorrect data");
                        return;
                    }
                    mOutput.write(data.getBytes());
                    mOutput.close();
                    showDialog("записано: " + data);
                } catch (FileNotFoundException ea) {
                    ea.printStackTrace();
                    showDialog(ea.getMessage());
                } catch (IOException ee) {
                    ee.printStackTrace();
                    showDialog(ee.getMessage());
                }

            }
        });

    }

    void showDialog(String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(message).setTitle("Result").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void getTranslation(View view)
    {
        EditText etWord = findViewById(R.id.word);

        try {
            InputStream in = openFileInput(NOTES);

            if (in != null) {
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                String str = "";
                String[] dict = new String[100];
                int i = 0;

                while ((str = reader.readLine()) != null) {
                    dict[i] = str;
                    i++;
                }

                in.close();
                etWord.setText(search(dict, etWord.getText().toString()));
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    String search(String[] arr, String query)
    {
        for (String s:arr
             ) {
            if (s==null)
                return "not found";
            if (s.contains(query))
                return s;
        }
        return "not found";
    }

}
