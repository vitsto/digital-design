package com.digdes.school;

public class Main {
    public static void main(String[] args) {
        JavaSchoolStarter starter = new JavaSchoolStarter();
        try {
            //Вставка строки в коллекцию
            starter.execute("INSERT VALUES 'lastName' = 'Федоров ', 'id'= 3, 'age'=40, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Столяров', 'id'= 5, 'age'=28, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Мышкин', 'id'= 9, 'cost' = 7.33 , 'age'=35, 'active'=false");
            starter.execute("INSERT VALUES 'lastName' = 'Иванов', 'id'= 11, 'cost' = 4.00 , 'age'=45, 'active'=false");
            starter.execute("INSERT VALUES 'lastName' = 'Петрова', 'id'= 12, 'cost' = 9 , 'age'=38, 'active'=true");
            starter.execute("INSERT VALUES 'lastName' = 'Петров', 'id'= 13, 'cost' = 12 , 'age'=25, 'active'=false");
//            List<Map<String, Object>> result1 = starter.execute("      INSERT          VALUES" );
            //Изменение значения которое выше записывали
//            List<Map<String, Object>> result2 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3");
//            //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
//            List<Map<String, Object>> result3 = starter.execute("SELECT");

            //-----------------------------------
//            System.out.println(starter.execute("select"));
//            System.out.println(starter.execute("select where 'cost'!=5"));
//            System.out.println(starter.execute("select where 'lastname' like '%ров%' and 'age' < 38"));
            // ----------------------------------------------------
//            System.out.println(starter.execute("select where ('ID' < 9 and 'lastname' ilike 'СТОЛЯРОВ') or 'cost' > 5"));
//            System.out.println(starter.execute("select where 'active' =    true"));
//            System.out.println(starter.execute("delete where 'cost'=null"));
//            System.out.println(starter.execute("delete where 'age'>35 or 'age' < 27"));
//            System.out.println(starter.execute("delete"));

            System.out.println(starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3"));
            System.out.println(starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}