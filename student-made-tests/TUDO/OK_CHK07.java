/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_CHK07 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class Person {
      private       String course;
      private       int number;
      private       int age;
      private       String name;
      public String getCourse () {
         return this.course;
      }
      public void setCourse (String course) {
         this.course = course;
      }
      public int getNumber () {
         return this.number;
      }
      public void setNumber (int number) {
         this.number = number;
      }
      public void init (String name, int age) {
         this.name = name;
         this.age = age;
      }
      public String getName () {
         return this.name;
      }
      public int getAge () {
         return this.age;
      }
      public void print () {
         System.out.println("Name = " + this.name);
         System.out.println("Age = " + this.age);
      }
   }

}
