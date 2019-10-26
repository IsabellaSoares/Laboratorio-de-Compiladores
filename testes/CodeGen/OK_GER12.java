/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER12 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m1 () {
         System.out.print(1 + " ");
      }
      public void m2: () {
         System.out.print(n + " ");
      }
   }

   private static class B {
      override void m2: () {
         System.out.print(n + " ");
;
      }
   }

   private static class C {
      override void m1 () {
;
         System.out.print(2 + " ");
      }
      public void m3 () {
;
         System.out.print(1 + " ");
         System.out.print(2 + " ");
      }
   }

   private static class Program {
      public void run () {
         C a;
         C b;
         C c;
         System.out.println("1 2 1 2 1 2 1 2");
         b = new B();
         b.m2:(1);
         c = new C();
         c.m1();
         c.m3();
      }
   }

}
