package org.example.service;

import org.example.model.CallData;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CallDataService {
    private HashMap<String, List<CallData>> callDataMap = new HashMap<>();


    private String search(String splitLine[], String regex, int n){
        Pattern pattern = Pattern.compile(regex);
        String result = null;

        for(int i = 0; i < splitLine.length; i++){
            Matcher matcher = pattern.matcher(splitLine[i]);

            if(matcher.matches() && n-- == 0){
                result = matcher.group();
                break;
            }
        }

        return result;
    }


    private void setDateTimes(CallData callData, String dateTime1, String dateTime2){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        callData.setDateTimeCallStart(LocalDateTime.parse(dateTime1, formatter));
        callData.setDateTimeCallEnd(LocalDateTime.parse(dateTime2, formatter));
    }


    private CallData createRecord(String splitLine[]){
        CallData callData = new CallData();

        callData.setPhoneNumber(search(splitLine, "\\d{11}", 0));
        callData.setCallType(search(splitLine, "01|02", 0));
        callData.setFareType(search(splitLine, "06|03|11", 0));

        String dateTime1 = search(splitLine, "\\d{14}", 0);
        String dateTime2 = search(splitLine, "\\d{14}", 1);

        if(Long.parseLong(dateTime1) > Long.parseLong(dateTime2)){
            setDateTimes(callData, dateTime2, dateTime1);
        }
        else{
            setDateTimes(callData, dateTime1, dateTime2);
        }

        return callData;
    }


    private void add(String splitLine[]){
        CallData callData = createRecord(splitLine);

        String phoneNumber = callData.getPhoneNumber();

        if(!callDataMap.containsKey(phoneNumber)){
            callDataMap.put(phoneNumber, new ArrayList<>());
        }

        callDataMap.get(phoneNumber).add(callData);
    }


    public void loadCDR(String path) {
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);

            String line = reader.readLine();
            while (line != null) {
                String splitLine[] = line.split("\\s*(\\s|,|!|\\.|:|;|\\|)\\s*");
                add(splitLine);
                line = reader.readLine();
            }

            reader.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private LocalTime calculateDuration(CallData callData){
        LocalDateTime callEnd = callData.getDateTimeCallEnd();
        LocalDateTime callStart = callData.getDateTimeCallStart();

        LocalDateTime diffTime = callEnd.minusSeconds(callStart.getSecond())
                .minusMinutes(callStart.getMinute()).minusHours(callStart.getHour());

        return LocalTime.of(diffTime.getHour(), diffTime.getMinute(), diffTime.getSecond());
    }


    private String dateTimeFormat(LocalDateTime dateTime){
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    private double tariff(String callType, String fareType, int minutesUsed, int minute){
        double cost = 0;
        int tempMinute = minutesUsed + minute;

        if(fareType.equals("06") && tempMinute > 300){
            cost = minutesUsed > 300 ? minute : (tempMinute - 300);
        } else if (fareType.equals("03")) {
            cost = 1.5 * minute;
        } else if (fareType.equals("11") && callType.equals("01")) {
            if(tempMinute > 100){
                cost += minutesUsed > 100 ? 1.5 * minute : (tempMinute - 100) * 1.5;
            } else {
                cost += 0.5 * minute;
            }
        }

        return cost;
    }


    // извиняюсь за эту функцию, мне нужно научиться придумывать более локаничные функции
    // я стараюсь, но не всегда получается
    private void createReport(String phoneNumber, List<CallData> callDataList, int i){
        try(FileWriter writer = new FileWriter("reports/report" + i + ".txt", false))
        {
            int minutesUsed = 0;
            double totalCost = 0;

            writer.write("Tariff index: " + callDataList.get(0).getFareType() + "\n");
            writer.write("----------------------------------------------------------------------------\n");
            writer.write("Report for phone number "+ phoneNumber + ":\n");
            writer.write("----------------------------------------------------------------------------\n");
            writer.write("| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n");
            writer.write("----------------------------------------------------------------------------\n");

            for(int j = 0; j < callDataList.size(); j++) {
                CallData callData = callDataList.get(j);

                LocalTime time = calculateDuration(callData);
                int minute = time.getMinute() + (time.getSecond() > 0 ? 1 : 0);

                if (callData.getFareType().equals("11") && callData.getCallType().equals("02")) {
                    minute = 0;
                }

                double cost = tariff(callData.getCallType(), callData.getFareType(), minutesUsed, minute);
                totalCost += cost;
                minutesUsed += minute;

                writer.write("|     " + callData.getCallType() + "    | " + dateTimeFormat(callData.getDateTimeCallStart())
                        + " | " + dateTimeFormat(callData.getDateTimeCallEnd()) + " | " + time + " |  " + cost + " |\n");
            }

            if(callDataList.get(0).getFareType().equals("06")){
                totalCost += 100;
            }

            writer.write("----------------------------------------------------------------------------\n");
            writer.write("|                                           Total Cost: |     " + totalCost + " rubles |\n");
            writer.write("----------------------------------------------------------------------------\n");

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }


    public void createReports(){
        Iterator<HashMap.Entry<String, List<CallData>>> iterator = callDataMap.entrySet().iterator();
        for (int i = 0; ; ++i) {
            if(iterator.hasNext()) {
                HashMap.Entry<String, List<CallData>> entry = iterator.next();
                createReport(entry.getKey(), entry.getValue(), i);
            } else break;
        }
    }

}