package com.example.arakjel.foxsnotes.managers;

import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LoggerManager {

    private File dir;
    private File file;
    private static final String filename = "diary.txt";
    private static final String folder = "Foxs Notes";
    private static final File path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS);
    public static ArrayList<String> listOfDiaryLines;

    public LoggerManager() {
        listOfDiaryLines = new ArrayList<String>();
    }

    public void create() {
        createFolder();
        createFile();
    }

    private void createFolder() {
        dir = new File(path, folder);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private void createFile() {
        file = new File(dir, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(String message) {
        try {
            create();
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.append(message);
            outputStreamWriter.append("\r\n");
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read() {
        try {
            create();
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            listOfDiaryLines.clear();
            while ((line = bufferedReader.readLine()) != null) {
                listOfDiaryLines.add(line);
                stringBuffer.append(line).append('\n');
            }
            fileInputStream.close();
            inputStreamReader.close();
            return stringBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void clean() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.append("");
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAndCreate(List<String> list) {
        try {
            clean();
            create();
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            for (int i=0; i<list.size(); i++) {
                outputStreamWriter.append(list.get(i));
                outputStreamWriter.append("\r\n");
            }
            outputStreamWriter.flush();
            outputStreamWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFileEmpty() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            if (br.readLine() == null) {
                System.out.println("No errors, and file empty");
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isThereAnyMessage() {
        return listOfDiaryLines.isEmpty();
    }

    private String getMessage(String message) {
        String tmp = DateFormat.getDateTimeInstance().format(new Date());
        return tmp + " - " + message;
    }
}
