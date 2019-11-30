/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER01 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
         System.out.print(7 + " ");

         if ((1 > 0)) {
            System.out.print(0 + " ");
         }
         else {
         }

         if ((1 >= 0)) {
            System.out.print(1 + " ");
         }
         else {
         }

         if ((1 != 0)) {
            System.out.print(2 + " ");
         }
         else {
         }

         if ((0 < 1)) {
            System.out.print(3 + " ");
         }
         else {
         }

         if ((0 <= 1)) {
            System.out.print(4 + " ");
         }
         else {
         }

         if ((0 == 0)) {
            System.out.print(5 + " ");
         }
         else {
         }

         if ((0 >= 0)) {
            System.out.print(6 + " ");
         }
         else {
         }

         if ((0 <= 0)) {
            System.out.print(7 + " ");
         }
         else {
         }

         if ((1 == 0)) {
            System.out.print(18 + " ");
         }
         else {
         }

         if ((0 > 1)) {
            System.out.print(10 + " ");
         }
         else {
         }

         if ((0 >= 1)) {
            System.out.print(11 + " ");
         }
         else {
         }

         if ((0 != 0)) {
            System.out.print(12 + " ");
         }
         else {
         }

         if ((1 < 0)) {
            System.out.print(13 + " ");
         }
         else {
         }

         if ((1 <= 0)) {
            System.out.print(14 + " ");
         }
         else {
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
