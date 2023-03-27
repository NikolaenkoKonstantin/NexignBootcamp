package org.example;

import org.example.service.CallDataService;

public class Main {
    public static void main(String[] args) {

        CallDataService callDataService = new CallDataService();

        callDataService.loadCDR("cdr.txt");

        callDataService.createReports();
    }
}