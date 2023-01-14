package com.example.tobispring;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TddTest {
    @DisplayName("ApplicationContext study test")
    @Test
    void testApplicationReferenceGetBean() {
        // given
//        ApplicationContext config = new AnnotationConfigApplicationContext();

        // when
//        config.getBean("a");

        // then

        blog();
    }

    static class User {
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        /*@Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            User user = (User) o;
            return age == user.age && Objects.equals(name, user.name);
        }*/

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

    void blog() {
        User user1 = new User("홍길동", 20);
        User user2 = new User("홍길동", 20);
//        user2 = user1;
//        Set<User> userSet = new HashSet<>(List.of(user1, user2));

//        System.out.println(userSet.size());

//        VM.current().addressOf(nutritionFacts1)

        // 이거 왜이럼? hashCode( ) 함수만 재정의 했더니 메모리 주소랑 해시 코드 모두 같아지는데도
        // equals( ) 메서드에서 false가 발생한다. 뭐가 문제지. Object#equals( ) 메서드를 뜯어봐야 될듯
        System.out.println(user1.toString());
        System.out.println(user2);
        System.out.println(user1.hashCode());
        System.out.println(user2.hashCode());
        System.out.println("identityHashCode : " + System.identityHashCode(user1));
        System.out.println("identityHashCode : " + System.identityHashCode(user2));
        System.out.println("isEquals : " + user1.equals(user2));
        /*System.out.println(user1 == user2);
        System.out.println(user1);
        System.out.println(user2);*/
    }
}
