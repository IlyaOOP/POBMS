package kovalenkoilya.fit.bstu.lab1;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText username=null;
    private EditText  password=null;
    private Button login;
    private int CountOfAttempt = 0;
    private String pass = "";
    private boolean sended = false;
    private boolean attempUsed = false;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    { super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_main);
        username=(EditText) findViewById (R.id.editText1);
        password=(EditText) findViewById (R.id.editText2);
        login = (Button) findViewById (R.id.button1);

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (sended==true) {
                    pass = "";
                    sended = false;
                }
                if (event.getAction()==KeyEvent.ACTION_UP&&event.getKeyCode()!=61) {
                    pass += (char) event.getUnicodeChar();
                    password.setText(password.getText().toString().replaceAll("[\\s\\w]", "*"));
                    password.setSelection(password.getText().length());
                }
                return false;
            }
        });
    }

    public void login(View view) {
        sended=true;
        CountOfAttempt++;
        if(username.getText().toString().equals("admin") && pass.equals("admin")){
            Toast.makeText(getApplicationContext(),"You are welcome!", Toast.LENGTH_SHORT).show();
        }
        else if(getDiff("admin", pass)==1&&attempUsed==false)
        {
            Toast.makeText(getApplicationContext(),"Rong Credentials. input extended", Toast.LENGTH_SHORT).show();
            CountOfAttempt-=2;
            attempUsed=true;
            password.setText("");
        }
        else {
            Toast.makeText(getApplicationContext(),"Rong Credentials", Toast.LENGTH_SHORT).show();
            if (CountOfAttempt==4) {
                login.setEnabled(false);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            password.setText("");
        }
    }

    private int getDiff(String a, String b)
    {
        int res = 0;
        if (b.length()>a.length())
        {
            String c = a;
            a=b;
            b=c;
        }
        char[] ca = a.toCharArray();
        char[] cb = b.toCharArray();
        if (ca.length-cb.length==1)
            res++;
        if (ca.length-cb.length>1)
            res=ca.length-cb.length;
        for (int i = 0; i<ca.length-1; i++)
        {
            if (i<cb.length)
                if (ca[i]!=cb[i])
                    res++;
        }
        return res;
    }
}
