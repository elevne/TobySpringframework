package org.example.thirdchapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {

    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            int ret = callback.doSomethingWithReader(br);
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
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

    public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            T res = initVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                res = callback.doSomethingWithLine(line, res);
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

//    public Integer calcSum(String filepath) throws IOException {
//
//        BufferedReader reader = null;
//
//        try {
//            reader = new BufferedReader(new FileReader(filepath));
//            Integer sum = 0;
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sum += Integer.valueOf(line);
//            }
//            return sum;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }


//    public Integer calcSum(String filepath) throws IOException {
//        BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
//            @Override
//            public Integer doSomethingWithReader(BufferedReader reader) throws IOException {
//                Integer sum = 0;
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    sum += Integer.valueOf(line);
//                }
//                return sum;
//            }
//        };
//        return fileReadTemplate(filepath, sumCallback);
//    }
//
//    public Integer calcMultiply(String filepath) throws IOException {
//        BufferedReaderCallback multiplyCallback = new BufferedReaderCallback() {
//            @Override
//            public Integer doSomethingWithReader(BufferedReader reader) throws IOException {
//                Integer multiply = 1;
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    multiply *= Integer.valueOf(line);
//                }
//                return multiply;
//            }
//        };
//        return fileReadTemplate(filepath, multiplyCallback);
//    }


    public Integer calcSum(String filepath) throws IOException {
        LineCallback<Integer> sumCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value){
                return value + Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, sumCallback, 0);
    }

    public Integer calcMultiply(String filepath) throws IOException {
        LineCallback<Integer> multiplyCallback = new LineCallback<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return value * Integer.valueOf(line);
            }
        };
        return lineReadTemplate(filepath, multiplyCallback, 1);
    }

    public String concatenate(String filepath) throws IOException {
        LineCallback<String> concatCallback = new LineCallback<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }

        };
        return lineReadTemplate(filepath, concatCallback, "");
    }

}
