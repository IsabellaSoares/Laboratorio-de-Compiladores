/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM14 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
   }

   private static class B extends A {
   }

   private static class Program {
      public void run () {
         A a, a2;
         B b;
         a = null;
         b = null;
         a2 = null;

         if ((a == b)) {
            System.out.println(0);
         }
         else {
         }

         if ((b == a)) {
            System.out.println(1);
         }
         else {
         }

         if ((a == a2)) {
            System.out.println(2);
         }
         else {
         }
      }
   }

}
