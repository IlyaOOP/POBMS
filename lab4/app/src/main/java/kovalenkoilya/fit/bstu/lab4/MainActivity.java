package kovalenkoilya.fit.bstu.lab4;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends ListActivity {

    private List<String> item = null;
    private List<String> path = null;
    private String root;
    private TextView myPath;
    SecretKeySpec SKS = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////
        FileOutputStream fOut = null;
        //Since you are creating a subdirectory, you need to make sure it's there first
        File directory = new File(Environment.getExternalStorageDirectory(), "AutoWriter");
        if (!directory.exists()) {
            boolean flag = directory.mkdir();
            Log.i("direrr", flag+"");
        }
        Toast.makeText(this, "creted: "+directory, Toast.LENGTH_SHORT).show();

        try {
            File f = new File(directory, "samplefile.txt");
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("errFile", e.getMessage());
        }

        String nochartOutput = "See my collection!";

        OutputStreamWriter osw = new OutputStreamWriter(fOut);
        try {
            String cryptotext = encrypt(nochartOutput);
            osw.write(cryptotext);

            osw.flush();
            osw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        ////
        myPath = (TextView)findViewById(R.id.path);

        root = Environment.getExternalStorageDirectory().getPath();

        getDir(root);
    }

    private void getDir(String dirPath)
    {
        myPath.setText("Location: " + dirPath);
        item = new ArrayList<String>();
        path = new ArrayList<String>();
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if(!dirPath.equals(root))
        {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(f.getParent());
        }

        for(int i=0; i < files.length; i++)
        {
            File file = files[i];

            if(!file.isHidden() && file.canRead()){
                path.add(file.getPath());
                if(file.isDirectory()){
                    item.add(file.getName() + "/");
                }else{
                    item.add(file.getName());
                }
            }
        }

        ArrayAdapter<String> fileList =
                new ArrayAdapter<String>(this, R.layout.row, item);
        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        File file = new File(path.get(position));

        if (file.isDirectory())
        {
            if(file.canRead()){
                getDir(path.get(position));
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK", null).show();
            }
        }else {
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader in = new InputStreamReader(fis);
                String data = "";
                String res = "{Encrypted}: ";
                int i=-1;
                while((i=in.read())!=-1){
                    data+=(char)i;
                }
                res+=data;
                res+="\n{decrypted}:";
                res+=decrypt(data);
                        new AlertDialog.Builder(this)
                                .setTitle("[" + file.getName() + "]")
                                .setMessage(res)
                                .setPositiveButton("OK", null).show();
            }
            catch (Exception ex)
            {
                Log.e("ReadError", ex.getLocalizedMessage());
            }

        }
    }

    private String encrypt(String data)
    {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            SKS = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e("Crypto", "AES secret key spec error");
        }

        // Encode the original data with AES
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, SKS);
            encodedBytes = c.doFinal(data.getBytes());
        } catch (Exception e) {
            Log.e("Crypto", "AES encryption error");
        }
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    private String decrypt(String data)
    {
        if (SKS==null)
            return "cant decrypt";
        // Decode the encoded data with AES
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, SKS);
            decodedBytes = c.doFinal(Base64.decode(data, Base64.DEFAULT));
        } catch (Exception e) {
            Log.e("Crypto", "AES decryption error:"+e.getMessage());
        }
        return new String(decodedBytes);
    }

}

