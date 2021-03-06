package kovalenkoilya.fit.bstu.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ShellExecuter SE;
    ListView lv;
    TextView outputTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SE = new ShellExecuter();
        lv = findViewById(R.id.listView);
        outputTV = findViewById(R.id.outputTV);
    }

    public void onAllAppsClick(View view)
    {
        String result = SE.Executer("ls -m /system/app");//system/data/app
        String[] apps = result.split(",");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, apps);
        lv.setAdapter(adapter);

        outputTV.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
    }

    public void onTimeClick(View view)
    {
        String result = SE.Executer("date +%T");
        outputTV.setText("Текущее время:"+result);

        outputTV.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);
    }

    public void onProcessorClick(View view)
    {
        String result = ((SE.Executer(/*"tail -n 3*/"cat /proc/cpuinfo").split("name"))[1].split("\n"))[0];
        outputTV.setText("Модель процессора"+result);

        outputTV.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);
    }

    public void onFindFileclick(View view)
    {
        String result = SE.Executer("find /sdcard/Download/file.txt");
        if (result.length()>1)
            result+="файл существует";
        outputTV.setText(result);

        outputTV.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);
    }

    public void onProcessClick(View view)
    {
        String result = "Process count: "+ Integer.toString((SE.Executer("ps").split("\n")).length);

        outputTV.setText(result);

        outputTV.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);
    }

    public void onMemoryClick(View view)
    {
        String result = SE.Executer("df");

        outputTV.setText(result);

        outputTV.setVisibility(View.VISIBLE);
        lv.setVisibility(View.GONE);
    }
}
