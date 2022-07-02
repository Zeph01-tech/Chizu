package com.discord;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class Test {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        String video_url = in.next();
        in.close();
        URL url = new URL("https://yt1s.io/api/ajaxSearch");
        String postData = "q="+video_url+"&vt=home";
        byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        BufferedReader hmm = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        System.out.println(hmm.readLine());
    }
}