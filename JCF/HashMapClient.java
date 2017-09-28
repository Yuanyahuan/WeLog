package com.william.abner.javacollections;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashMapClient {

    @Test
    public void selfDefineObjectAsKey(){
        HashMap<SelfDefineObject,Integer> hashMap = new HashMap<SelfDefineObject, Integer>();
        SelfDefineObject obj = new SelfDefineObject(10);
        hashMap.put(obj,10);
        System.out.println(hashMap.get(obj));

        System.out.println("----*********----------");

        obj.setAge(66);
        System.out.println(hashMap.get(obj));
    }

    class SelfDefineObject{
        private int age;

        public SelfDefineObject(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(age).toHashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(null==obj)
                return false;
            if(this==obj)
                return true;
            if(this.getClass()!=obj.getClass())
                return false;
            SelfDefineObject target = (SelfDefineObject) obj;
            return new EqualsBuilder().append(this.age,target.age).isEquals();
        }
    }

}