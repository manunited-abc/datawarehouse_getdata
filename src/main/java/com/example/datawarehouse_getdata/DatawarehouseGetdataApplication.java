package com.example.datawarehouse_getdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootApplication
public class DatawarehouseGetdataApplication {

    public static void main(String[] args) throws IOException {
        //Test 1
        LocalDateTime startDate = LocalDateTime.of(2022, 8, 15, 18, 0,0);
        ArrayList<Lottery> lotteries = GetData1.getDataFromDates(startDate, "https://ketqua.me/");
        for(Lottery l:lotteries) {
            System.out.println(l);
        }
        Path path = Paths.get("uploads");
        WriteFile.write(path,path.toAbsolutePath()+"\\datawarehouse.csv", lotteries);
        //SpringApplication.run(DatawarehouseGetdataApplication.class, args);


    }

}
