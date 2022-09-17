package com.example.datawarehouse_getdata;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//Lay du lieu website https://ketqua.me/xsmt-xo-so-mien-trung-ngay-...
public class GetData1 {
    //Lay du lieu tu ngau startDate toi now
    public static ArrayList<Lottery> getDataFromDates(LocalDateTime startDate, String url) throws IOException {
        ArrayList<Lottery> result = new ArrayList<>();
        while(startDate.isBefore(LocalDateTime.now())) {
            ArrayList<Lottery> list = extractContentHTML(url, startDate);
            startDate = startDate.plusDays(1);
            result.addAll(list);
        }
        return result;
    }
    //Phan tich cu phap trang html
    public static ArrayList<Lottery> extractContentHTML(String url, LocalDateTime releaseDate) throws IOException {
        int year = releaseDate.getYear();
        int month = releaseDate.getMonthValue();
        int day = releaseDate.getDayOfMonth();
        if (releaseDate.isBefore(LocalDateTime.of(year,month,day,17,10,00,00))) {
            System.out.println("chua so");
            releaseDate.minusDays(1);
        }
        String dateString = convertDateToString(releaseDate);
        System.out.println(dateString);
        String fullUrl = url + "xsmt-xo-so-mien-trung-ngay-" + dateString;
        Document document = Jsoup.connect(fullUrl).get();
        Element tableParent = document.getElementsByClass("table-tructiep").first();
        Element header = tableParent.getElementsByClass("result-header").first();

        String company = header.getElementsByTag("a").first().text();
        Element tableResult = tableParent.getElementsByClass("table").first();
        Element thead = tableResult.getElementsByTag("thead").first();
        Element tbody = tableResult.getElementsByTag("tbody").first();

        Elements provinces = thead.getElementsByTag("th");
        int size = provinces.size();
        ArrayList<Lottery> lotteries = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            Lottery lottery = new Lottery();
            lottery.setCompany(company);
            lottery.setProvince(provinces.get(i).text());
            lottery.setIssueDate(releaseDate.toString());
            // Get value pize 8
            lottery.setPrize8(getSerial(i, tbody, 0));
            // Get value pize 7
            lottery.setPrize7(getSerial(i, tbody, 1));
            // Get value pize 6
            lottery.setPrize6(getSerial(i, tbody, 2));
            // Get value pize 5
            lottery.setPrize5(getSerial(i, tbody, 3));
            // Get value pize 4
            lottery.setPrize4(getSerial(i, tbody, 4));
            // Get value pize 3
            lottery.setPrize3(getSerial(i, tbody, 5));
            // Get value pize 2
            lottery.setPrize2(getSerial(i, tbody, 6));
            // Get value pize 1
            lottery.setPrize1(getSerial(i, tbody, 7));
            // Get value pize 0
            lottery.setPrize0(getSerial(i, tbody, 8));

            lotteries.add(lottery);
        }
        return lotteries;

    }
    //Noi cac day so ngan cac boi dau "-"
    public static String getSerial(int index, Element elementParent, int i) {
        Element trowElements = elementParent.getElementsByTag("tr").get(i);
        Elements serialsElementsPrize = trowElements.getElementsByTag("td").get(index).children();
        ArrayList<String> serialListPrize = new ArrayList<>();
        for (Element serialElement : serialsElementsPrize) {
            serialListPrize.add(serialElement.text());
        }
        String serialPrize = String.join("-", serialListPrize);
        return serialPrize;
    }
    //Format thoi gian thanh dang dd-mm-yyyy
    public static String convertDateToString(LocalDateTime date) {
        DateTimeFormatter fmDateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(fmDateTimeFormatter);
    }

}
