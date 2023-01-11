package com.example.tobispring.chap05.templatecallback;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public int calcSum(String filepath) throws IOException {
        LineCallback<Integer> callback = (value, line) -> value + Integer.parseInt(line);

        return lineReadTemplate(filepath, callback, 0);
    }

    public int calcMultiply(String filepath) throws IOException {
        LineCallback<Integer> callback = (value, line) -> value * Integer.parseInt(line);

        return lineReadTemplate(filepath, callback, 1);
    }

    public String concatenate(String filepath) throws IOException {
        LineCallback<String> callback = (value, line) -> value + line;

        return lineReadTemplate(filepath, callback, "");
    }

    public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            T result = initVal;
            String line = null;
            while((line = br.readLine()) != null) {
                result = callback.doSomethingWithLine(result, line);
            }
            return result;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            return callback.doSomethingWithReader(br);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw ex;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
