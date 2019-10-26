/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER07 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
         System.out.println(0);
      }
   }

   private static class Program {
      public void run () {
         A a;
         System.out.println("0");
         a = new A();
         a.m();
      }
   }

}
