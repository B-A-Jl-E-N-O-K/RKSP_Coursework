package com.example.rksp_coursework.services;


import com.example.rksp_coursework.models.SecSupply;
import com.example.rksp_coursework.models.Securities;
import com.example.rksp_coursework.repositories.SecSupRepository;
import com.example.rksp_coursework.repositories.SecuritiesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.transaction.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;




@Service
@Transactional
public class SecuritiesService {
    private final SecuritiesRepository secRep;
    private final SecSupRepository secSupRep;


    public SecuritiesService(SecSupRepository secSupRep, SecuritiesRepository secRep) {
        this.secSupRep = secSupRep;
        this.secRep = secRep;
    }




    @Scheduled(fixedDelayString = "PT15M")
    public void startConnectApi(){
        //Создание записи о начале поставки
        LocalDateTime localDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String stringDateTime = localDateTime.format(formatter);
        SecSupply newSup = new SecSupply(stringDateTime);

        secSupRep.save(newSup);

        SecSupply currSup = secSupRep.findFirstByOrderByIdDesc();
        int currSupId = currSup.getId();

        //Начало добавления списков инвестиционных инструментов
        List<Securities> secList = new ArrayList<>();
        List<Securities> secFinalList = new ArrayList<>();

        try {

            String URL = "https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.xml?iss.meta=off&first=10&securities.columns=SECID,SECNAME&marketdata.columns=SECID,LAST";

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(URL);

            // normalize XML response
            doc.getDocumentElement().normalize();


            //read blocks of lists
            NodeList blocksList = doc.getElementsByTagName("rows");



            //loop all available blocks
            for (int i = 0; i < 2; i++) {
                Node rowsListNode = blocksList.item(i);
                Element rowsListEl = (Element) rowsListNode;
                NodeList rowsList = rowsListEl.getElementsByTagName("row");
                for (int j = 0; j < rowsList.getLength(); j++) {
                    Node rowNode = rowsList.item(j);
                    Element row = (Element) rowNode;
                    if (i == 0)
                    {
                        secList.add(new Securities(currSupId, row.getAttribute("SECID"),
                                row.getAttribute("SECNAME"), -1));
                    }
                    else
                    {
                        String str_id = row.getAttribute("SECID");
                        Securities currSec = secList.get(j);
                        if(!row.getAttribute("LAST").isEmpty()){
                            double price = Double.parseDouble(row.getAttribute("LAST"));
                            if (currSec.getStrId().equals(str_id)) {
                                currSec.setPrice(price);

                            }
                        }
                        secFinalList.add(currSec);

                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        saveSupply(secFinalList);
        secList.clear();
        secFinalList.clear();

        deleteOldData();

        createVisualization();


    }

    public void saveSupply(List<Securities> supply) {
        secRep.saveAll(supply);
    }

    public void deleteOldData() {
        if(secSupRep.count() > 100){
            SecSupply oldestSupply = secSupRep.findFirstByOrderById();
            int oldId = oldestSupply.getId();
            secRep.deleteAllBySupId(oldId);
            secSupRep.deleteById(oldId);
        }

    }

    public void createVisualization() {

        SecSupply newestSupply = secSupRep.findFirstByOrderByIdDesc();
        int newestSupId = newestSupply.getId();
        int countSecInNewestSup = secRep.countBySupId(newestSupId);

        List<Securities> secList = secRep.findAllBySupId(newestSupId);

        for (int i=0; i<countSecInNewestSup; i++){

            Securities currSup = secList.get(i);
            List<Securities> currSecList = secRep.findAllByStrId(currSup.getStrId());
            GraphCreation graph = new GraphCreation(currSecList);

        }

    }

    public String getCurrSecJson(){

        SecSupply newestSupply = secSupRep.findFirstByOrderByIdDesc();
        int newestSupId = newestSupply.getId();
        List<Securities> secList = secRep.findAllBySupId(newestSupId);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        for (int i=0; i<secList.size(); i++){
            try{
                json += ow.writeValueAsString(secList.get(i));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return json;
    }

    public List<Securities> getCurrSupply(){

        SecSupply newestSupply = secSupRep.findFirstByOrderByIdDesc();
        int newestSupId = newestSupply.getId();
        List<Securities> secList = secRep.findAllBySupId(newestSupId);

        return secList;
    }

    public List<Securities> getSecNamed(String strId){

        List<Securities> namedSecList = secRep.findAllByStrId(strId);

        return namedSecList;
    }

    public String getImgNamed(String strId){

        return "imgs/" + strId + ".png";

    }

    public String getGrowPercent(String strId){  //(Конечное значение – Начальное значение) / Начальное значение} × 100

        List<Securities> namedSecList = secRep.findAllByStrId(strId);
        Securities first = namedSecList.get(0);
        Securities last = namedSecList.get(namedSecList.size() - 1);
        double firstPrice = first.getPrice();
        double lastPrice = last.getPrice();

        double grow = (((lastPrice - firstPrice) / firstPrice) * 100);

        String result = String.format("%.2f",grow) + "%";


        return result;

    }

    public boolean getGrowRate(String strId){

        List<Securities> namedSecList = secRep.findAllByStrId(strId);
        Securities first = namedSecList.get(0);
        Securities last = namedSecList.get(namedSecList.size() - 1);
        double firstPrice = first.getPrice();
        double lastPrice = last.getPrice();

        double grow = (((lastPrice - firstPrice) / firstPrice) * 100);

        if (grow >= 0){
            return true;
        }

        return false;



    }



    public List<SecSupply> getSupList(){

        return secSupRep.findAll();

    }


    /*




    public Iterable<Dish> getAll() {
        return dishRepository.findAll();
    }

    public Iterable<Dish> getFilteredMenu(String type) {
        Iterable<Dish> allDishes = dishRepository.findAll();
        List<Dish> dishesList = new ArrayList<>();
        for (Dish dish: allDishes){
            if(dish.getType().equals(TypeDish.valueOf(type))){
                dishesList.add(dish);
            }
        }
        return dishesList;
    }

    public Dish getOne(int id) {
        return dishRepository.findById(id).get();
    }

    public void save(Dish dish) {
        dishRepository.save(dish);
    }

    public String update(int id, Dish newDish){
        Optional<Dish> optionalDish = dishRepository.findById(id);
        if (optionalDish.isEmpty()){
            return "Data not update.";
        }
        newDish.setId(id);
        dishRepository.save(newDish);
        return "Data update.";
    }

    public String delete(int id){
        Optional<Dish> optionalDish = dishRepository.findById(id);
        if (optionalDish.isEmpty()){
            return "Data not found";
        }
        dishRepository.delete(optionalDish.get());
        return "Data delete.";
    }*/
}
