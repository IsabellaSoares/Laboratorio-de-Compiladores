/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER01 {
   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
         System.out.print(7 + " ");

         if (1 > 0) {
            System.out.print(0 + " ");
         }

         if (1 >= 0) {
            System.out.print(1 + " ");
         }

         if (1 != 0) {
            System.out.print(2 + " ");
         }

         if (0 < 1) {
            System.out.print(3 + " ");
         }

         if (0 <= 1) {
            System.out.print(4 + " ");
         }

         if (0 == 0) {
            System.out.print(5 + " ");
         }

         if (0 >= 0) {
            System.out.print(6 + " ");
         }

         if (0 <= 0) {
            System.out.print(7 + " ");
         }

         if (1 == 0) {
            System.out.print(18 + " ");
         }

         if (0 > 1) {
            System.out.print(10 + " ");
         }

         if (0 >= 1) {
            System.out.print(11 + " ");
         }

         if (0 != 0) {
            System.out.print(12 + " ");
         }

         if (1 < 0) {
            System.out.print(13 + " ");
         }

         if (1 <= 0) {
            System.out.print(14 + " ");
         }
      }
   }

   private static class Program {
      public void run () {
         A a;
         System.out.println("7 0 1 2 3 4 5 6 7");
         a = new A();
         a.m();
      }
   }

}
