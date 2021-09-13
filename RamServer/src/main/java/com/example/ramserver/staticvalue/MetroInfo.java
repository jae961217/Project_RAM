package com.example.ramserver.staticvalue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

@Component
public class MetroInfo {
    private Map<Integer, Pair> metroInfo = new TreeMap<Integer,Pair>();

    private Map<String ,Vector<String>> linkedMap=new HashMap<>();
    private Map<String, Boolean> chk=new HashMap<>();

    /*@Resource(name = "testDAO")
    private TestDAO testDAO;
*/

    //json파일 전처리
    //metro 정보로 custom map 생성
    //두 역 사이의 최단경로를 찾기위한 전처리 과정
    @PostConstruct
    public void init() throws IOException, ParseException {
        ClassPathResource resource = new ClassPathResource("api/MetroInformation.json");
        JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(resource.getInputStream(), "UTF-8")); //json-simple
        JSONArray stationlist=(JSONArray)json.get("DATA");
        for(int i=0;i<stationlist.size();i++){
            JSONObject station= (JSONObject)stationlist.get(i);
            if(station.get("line_num").toString().equals("02호선")){
                //String stationCode=station.get("station_cd").toString();
                int stationCode=Integer.parseInt(station.get("station_cd").toString());
                Pair a=new Pair(station.get("line_num").toString(),station.get("station_nm").toString());
                metroInfo.put(stationCode,a);
                chk.put(station.get("station_nm").toString(),false);
            }
        }

        MakeMap();
       /* Object[] mapkey = metroInfo.keySet().toArray();
        Arrays.sort(mapkey);*/
        return;
    }

    //map 생성 , 노가다 ㅅㅂ, 현재는 2호선만 생성
    private void MakeMap(){
        for (Integer nKey : metroInfo.keySet())
        {
            if(!linkedMap.containsKey(nKey)){
                linkedMap.put(metroInfo.get(nKey).station_nm,new Vector<String>());
            }
            if(nKey>=202&&nKey<=242){
                //해당 키를 가지고 있는 역의 key
                //이 역의 전 역 이름 vector 저장
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(nKey-1).station_nm);
                //이 역의 다음 역 이름 vector 저장
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(nKey+1).station_nm);

            }
            else if(nKey==201){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(243).station_nm);
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(202).station_nm);
            }
            else if(nKey==243){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(201).station_nm);
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(242).station_nm);
            }
            else if(nKey==200){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(249).station_nm);
                System.out.println("jfsd");
            }
            else if(nKey==234){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(247).station_nm);
            }
            else if(nKey==247){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(234).station_nm);
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(248).station_nm);
            }
            else if(nKey==246){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(250).station_nm);
            }
            else if(nKey==250){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(246).station_nm);
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(245).station_nm);
            }
            else if(nKey==211){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(244).station_nm);
            }
            else if(nKey==244){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(211).station_nm);
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(245).station_nm);
            }
            else if(nKey==245){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(244).station_nm);
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(250).station_nm);
            }
            else if(nKey==249){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(248).station_nm);
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(200).station_nm);
            }
            else if(nKey==248){
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(249).station_nm);
                linkedMap.get(metroInfo.get(nKey).station_nm).add(metroInfo.get(247).station_nm);
            }
        }
    }
    //주어진 map으로 최단경로의 역을 찾아서 해당 역의 코드 return
    //BFS
    public String GetTradePlaceCode(String start,String destination){
        for (Integer nKey : metroInfo.keySet())
        {
            chk.put(metroInfo.get(nKey).station_nm,false);
        }


        Queue<QPair> q=new LinkedList<>();
        List<String>path=new LinkedList<>();
        path.add(start);
        chk.put(start,true);
        QPair newQpair=new QPair(start,path);
        q.offer(newQpair);


        while(!q.isEmpty()){

            QPair tQpair=q.poll();
            String here=tQpair.station_nm;

            System.out.println("next");
            //연결된 역 확인
            for(int i=0;i<linkedMap.get(here).size();i++){
                String nextStation=linkedMap.get(here).get(i);

                List<String> currentPath=new LinkedList<>();
                //다음 리스트 생성
                for(int j=0;j<tQpair.pathList.size();j++){
                    currentPath.add(tQpair.pathList.get(j));
                }

                //마지막 역으로 왔을때
                if(nextStation.equals(destination)){
                    int sz=currentPath.size();
                    //총 역의 개수가 짝수일때 start에 가까운 쪽 return
                    return currentPath.get(sz/2);
                }
                //방문한적 없을때
                if(chk.get(nextStation)==false){
                    chk.put(nextStation,true);
                    currentPath.add(nextStation);
                    QPair nextQPair=new QPair(nextStation,currentPath);
                    q.offer(nextQPair);
                }
            }
        }
        return "none";
    }

    public void resetWhiteList() {
        metroInfo.clear();
    }
}
class Pair{
    String line_num;
    String station_nm;
    public Pair(String line_num, String station_nm) {
        this.line_num = line_num;
        this.station_nm = station_nm;
    }
    public String first() {
        return line_num;
    }
    public String second() {
        return station_nm;
    }
}
//BFS 전용 클래스
class QPair{
    String station_nm;
    List<String> pathList;
    public QPair(String station_nm,List<String>pathList){
        this.station_nm=station_nm;
        this.pathList=pathList;
    }
}