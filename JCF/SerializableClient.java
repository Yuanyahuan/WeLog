package com.william.abner.javacollections;

import org.junit.Test;

import java.io.*;

public class SerializableClient {


    @Test
    public void serializablePatternRight() throws Exception {
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("./result.obj"));
        StaticFieldSerializable test = new StaticFieldSerializable();
        out.writeObject(test);
        out.flush();
        System.out.println(new File("./result.obj").length());
        out.writeObject(test);
        out.close();
        System.out.println(new File("./result.obj").length());

        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(
                "./result.obj"));
        StaticFieldSerializable t1 = (StaticFieldSerializable) oin.readObject();
        StaticFieldSerializable t2 = (StaticFieldSerializable) oin.readObject();
        oin.close();

        System.out.println(t1 == t2);
    }


    @Test
    public void serializablePatterError() throws Exception{
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("result.obj"));
        StaticFieldSerializable test = new StaticFieldSerializable();
        test.i = 1;
        out.writeObject(test);
        out.flush();
        test.i = 2;
        out.writeObject(test);
        out.close();
        ObjectInputStream oin = new ObjectInputStream(new FileInputStream(
                "result.obj"));
        StaticFieldSerializable t1 = (StaticFieldSerializable) oin.readObject();
        StaticFieldSerializable t2 = (StaticFieldSerializable) oin.readObject();
        System.out.println(t1.i);
        System.out.println(t2.i);
    }

}

class StaticFieldSerializable implements Serializable {

    private static final long serialVersionUID = 1L;

    public static int staticVar = 5;

    public int i;

    public static void main(String[] args) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream("./result.obj"));
            out.writeObject(new StaticFieldSerializable());
            out.close();

            StaticFieldSerializable.staticVar = 10;

            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(
                    "./result.obj"));
            StaticFieldSerializable t = (StaticFieldSerializable) oin.readObject();
            oin.close();

            System.out.println(t.staticVar);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
