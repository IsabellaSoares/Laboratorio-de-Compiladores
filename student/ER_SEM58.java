/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class ER_SEM58 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
   }

   private static class B {
   }

   private static class Program {
      public void run () {
         B a;
         B b;
         a = null;
         b = null;

         if (a != b) {
            System.out.println(0);
         }
      }
   }

}
