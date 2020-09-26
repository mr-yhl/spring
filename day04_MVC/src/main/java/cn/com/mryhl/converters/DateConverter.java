package cn.com.mryhl.converters;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义一个类型转换器，实现类型转换方法（必须实现Converter<原始类型,目标类型>接口）
 */
public class DateConverter implements Converter<String,Date> {

    public Date convert(String s){
        Date date = null;
        try{
           date= new SimpleDateFormat("yyyy-MM-dd").parse(s);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;

    }


}
