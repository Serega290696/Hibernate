package com.cybervision.market;

import com.cybervision.market.entity.User;
import com.cybervision.market.utils.ExpiredMap;
import com.cybervision.market.utils.impl.ExpiredMapImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class TestSuperHashMap extends Assert {

    /*

    Spring HibernateTemplate
    "следующим вашим желанием будет написать свое велосипедное DAO. Если вы фигачите в продакшен, от этого желания нужно избавиться и взять что-нибудь готовое. Например, Hibernate Generic Dao (у него кажется загнулся сайт, так что придется вам самим нагуглить документацию. В Maven Central он лежит, а исходник вот: https://github.com/based2/hibernate-generic-dao)"
    JavaConfig, ok?
    Книги: CleanCode, Java Persistence with Hiber., Java для про (рано?), (Concurrency)


    ---
    Netty
    WebSockets
    Concurrency
    Spring security

     */
    private ExpiredMap<String, User> superMap = new ExpiredMapImpl<>();
    private ExpiredMap<CustomKey, User> superMap2 = new ExpiredMapImpl<>();

    private User a;
    private User b;
    private User c;

    @Before
    public void setUp() throws Exception {
        a = new User("a@gmail.com", "qwe", 10d);
        b = new User("b@gmail.com", "qwe", 20d);
        c = new User("c@gmail.com", "qwe", 30d);
        superMap.put("a", a);
        superMap.put("b", b);
    }

    @Test
    public void testUsualPutAndGet() throws Exception {
        assertEquals(a.getEmail(), superMap.get("a").getEmail());
        assertEquals(b.getEmail(), superMap.get("b").getEmail());
    }

    @Test
    public void testGetAfterDefaultTimeExpire() throws Exception {
        superMap.put("c", c);
        assertNotNull(superMap.get("c"));
        Thread.sleep(6000);
        assertNotNull(superMap.get("c"));
        Thread.sleep(6000);
        assertNull(superMap.get("c"));
    }

    @Test
    public void testGetAfterCustomTimeExpire() throws Exception {
        superMap.put("c", c, 2000L);
        assertNotNull(superMap.get("c"));
        Thread.sleep(1500);
        assertNotNull(superMap.get("c"));
        Thread.sleep(2500);
        assertNull(superMap.get("c"));
    }

    @Test
    public void testMutableKey() throws Exception {
        CustomKey aaa = new CustomKey("name", 19);
        CustomKey bbb = new CustomKey("name", 20);

        superMap2.put(aaa, a);
        superMap2.put(bbb, b);
        System.out.println(superMap2.get(aaa));
        System.out.println(superMap2.get(bbb));
    }

    @Test
    public void testGetFromEmptyMap() throws Exception {
        ExpiredMap<String, User> superMap = new ExpiredMapImpl<>();
        superMap.get("a");
    }

    @Test
    public void testJavaVisualVM() throws Exception {

        Queue<String> s = new PriorityQueue<>(1);
        System.out.println("s: " + s.size());
        s.offer("a");
        System.out.println("s: " + s.size());
        s.offer("b");
        System.out.println("s: " + s.size());
        s.offer("c");
        System.out.println("s: " + s.size());
    }

    class StringHolder {
        private String string;

        public StringHolder(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    class CustomKey {
        private String name;
        private int age;

        public CustomKey(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public CustomKey() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CustomKey customKey = (CustomKey) o;

            if (age != customKey.age) return false;
            return name != null ? name.equals(customKey.name) : customKey.name == null;

        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + age;
//            return result;
            return 1;
        }
    }

}
