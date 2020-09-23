package cn.com.mryhl.createbeans;

//书厂
//静态工厂创建方式: 不用创建工厂的实例, 直接调用工厂类的一个静态方法来获取对象
//实例化工厂创建方式: 先创建工厂的实例, 再调用工厂实例的一个非静态方法来获取对象

//二者的区别就在于是否去   创建工厂的实例
public class BookFactory {

    //获取书
    public static Book getBook1() {
        return new Book();
    }
    //获取书
    public Book getBook2() {
        return new Book();
    }
}
