package cn.com.mryhl.createbeans;

public class CreateBean {


    public static void main(String[] args) {
        //我们常用的创建对象的方式有三种：
        //1. 直接使用new关键字创建
        Book book = new Book();

        //2. 使用静态工厂创建
        //需要我们知道1 类名  2 静态方法名
        Book book1 = BookFactory.getBook1();

        //3. 使用实例化工厂创建
        //需要1 先创建工厂实例    2 调用工厂实例的非静态方法
        BookFactory bookFactory = new BookFactory();
        Book book2 = bookFactory.getBook2();
    }
}
