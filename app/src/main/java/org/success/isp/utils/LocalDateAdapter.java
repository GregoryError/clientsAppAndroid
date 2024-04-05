package org.success.isp.utils;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.LocalDate;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateString = json.getAsString();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date date = sdf.parse(dateString);
            return new LocalDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Date date = src.toDate();
        return new JsonPrimitive(sdf.format(date));
    }
}




//import android.os.Build;
//
//import com.google.gson.JsonDeserializationContext;
//import com.google.gson.JsonDeserializer;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParseException;
//import com.google.gson.JsonPrimitive;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSerializer;
//
//import java.lang.reflect.Type;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.util.Date;
//import java.util.Locale;
//
//public class LocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
//    private static final String DATE_FORMAT = "yyyy-MM-dd";
//
//    @Override
//    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        String dateString = json.getAsString();
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
//            Date date = sdf.parse(dateString);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return null;
//    }
//
//    @Override
//    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
//        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
//        Date date = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            date = Date.from(src.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        }
//        return new JsonPrimitive(sdf.format(date));
//    }
//}














