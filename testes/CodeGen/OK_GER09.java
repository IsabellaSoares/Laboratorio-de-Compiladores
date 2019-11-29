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
      public void m1 (int n) {
         System.out.print(" n" + " ");
      }
   }

   private static class B extends A {
      public void m2 (int n) {
         super.m1(1);
         System.out.print(" n" + " ");
      }
   }

   private static class C extends B {
      public void m3 (int n) {
         super.m2(2);
         System.out.print(" n" + " ");
      }
      public void m4 (int n) {
         this.m3(3);
         System.out.println(" n" + " ");
      }
   }

   private static class Program {
      public void run () {
         C c;
         System.out.println("1 1 2 2 3 3 4 4");
         c = new C();
         c.m4(4);
      }
   }

}
