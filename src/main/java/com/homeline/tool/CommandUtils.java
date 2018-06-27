package com.homeline.tool;

import org.apache.http.util.TextUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandUtils {

    public static String exeCmd(String commandStr) {
        String result;
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec(commandStr);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            result = e.getMessage();
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    result = e.getMessage();
                    e.printStackTrace();
                }
            }
        }
        if (TextUtils.isEmpty(result)) {
            result = "ERROR";
        }
        return result;
    }

}
