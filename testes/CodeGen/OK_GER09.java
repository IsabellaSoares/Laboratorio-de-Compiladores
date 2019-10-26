/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER09 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m1: () {
         System.out.print( + " ");
      }
   }

   private static class B {
      public void m2: () {
;
         System.out.print( + " ");
      }
   }

   private static class C {
      public void m3: () {
;
         System.out.print( + " ");
      }
      public void m4: () {
;
         System.out.println( + " ");
      }
   }

   private static class Program {
      public void run () {
         C c;
         System.out.println("1 1 2 2 3 3 4 4");
         c = new C();
         c.m4:(4);
      }
   }

}
