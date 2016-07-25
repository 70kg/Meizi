package info.meizi_retrofit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr_Wrong on 15/12/6.
 */
public class Alone {
    int age;
    String name;

    public Alone(String name) {
        this.name = name;
    }

    public Alone() {
    }

    public Alone(int age) {
        this.age = age;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
//        String url = "http://pic.mmfile.net/2016/01/06r20.jpg";
//        http://i.meizitu.net/2016/07/23a10.jpg
//        http://i.meizitu.net/2016/07/20c47.jpg
        String url = "http://i.meizitu.net/2016/07/24b26.jpg";
        int i1 = url.lastIndexOf(".");

        String indexs = url.substring(i1 - 2, i1);
        System.out.println(String.format("%s%s%s", url.substring(0, i1 - 2),
                new DecimalFormat("00").format(Integer.valueOf(indexs) + 1), ".jpg"));
//        System.out.println(String.format("%s%s%s", url.substring(0, 33), new DecimalFormat("00").format(i), ".jpg"));


//        test(Alone.Person.class);
        Class<Alone> type = (Class<Alone>) Class.forName("info.meizi_retrofit.Alone");

//        Constructor<Alone> constructorStr = Alone.class.getConstructor(int.class);
//        Constructor<Alone> constructorStr2 = type.getConstructor(int.class);
//        Constructor<Alone> constructorStr1 = Alone.class.getConstructor(String.class);
//        System.out.println(constructorStr2.newInstance(10));
//        System.out.println(constructorStr1.newInstance("70kg"));

        HashMap<String, String> map = new HashMap<>();
        map.put("70kg", "aaa");
        map.put("71kg", "bbb");
        map.put("72kg", "ccc");
        map.put("73kg", "ddd");
        map.put("74kg", "eee");
        System.out.println(map);
        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry);
        }
    }

    @Override
    public String toString() {
        return "年级是" + this.age;
    }

    class Person {
        int age;

        public Person(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "age=" + this.age;
        }
    }

    private static <T> void test(Class<T> type1) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        Person.class.getTypeParameters();
//        Class<T> type = (Class<T>) Class.forName("Alone$Person");
        Constructor<T> constructor = type1.getDeclaredConstructor(Integer.class);
        System.out.println(constructor.newInstance(1));
    }

}
