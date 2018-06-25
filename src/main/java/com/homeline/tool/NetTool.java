package com.homeline.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;

public class NetTool {
	
	public static String ToStr(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)in, "utf-8"));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine())!=null){
            sb.append(line);
        }
        return sb.toString();
	}

}
