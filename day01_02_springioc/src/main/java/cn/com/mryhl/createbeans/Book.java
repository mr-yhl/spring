package cn.com.mryhl.createbeans;

import java.util.*;

public class Book {
    private String name;
    private float price;
    private Date publish;


    //集合, set  get  toString
    private String[] myArray;
    private List<String> myList;
    private Set<String> mySet;

    private Map<String,String> myMap;
    private Properties myProperties;


    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public Date getPublish() {
        return publish;
    }

    public String[] getMyArray() {
        return myArray;
    }

    public void setMyArray(String[] myArray) {
        this.myArray = myArray;
    }

    public List<String> getMyList() {
        return myList;
    }

    public void setMyList(List<String> myList) {
        this.myList = myList;
    }

    public Set<String> getMySet() {
        return mySet;
    }

    public void setMySet(Set<String> mySet) {
        this.mySet = mySet;
    }

    public Map<String, String> getMyMap() {
        return myMap;
    }

    public void setMyMap(Map<String, String> myMap) {
        this.myMap = myMap;
    }

    public Properties getMyProperties() {
        return myProperties;
    }

    public void setMyProperties(Properties myProperties) {
        this.myProperties = myProperties;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setPublish(Date publish) {
        this.publish = publish;
    }

    public Book() {
        System.out.println("书正在创建.....");
    }

    //全参构造函数
    public Book(String name, float price, Date publish) {
        this.name = name;
        this.price = price;
        this.publish = publish;

        Date date = new Date();
        this.publish = date;
    }


    public void init(){
        System.out.println("书创建之后....");
    }

    public void des(){
        System.out.println("书销毁之前....");
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", publish=" + publish +
                ", myArray=" + Arrays.toString(myArray) +
                ", myList=" + myList +
                ", mySet=" + mySet +
                ", myMap=" + myMap +
                ", myProperties=" + myProperties +
                '}';
    }
}
