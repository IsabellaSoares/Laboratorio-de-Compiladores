/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER03 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
         System.out.print(6 + " ");

         if ((true && true)) {
            System.out.print(1 + " ");
         }
         else {
         }

         if ((false && true)) {
            System.out.print(1000 + " ");
         }
         else {
         }

         if ((true && false)) {
            System.out.print(1000 + " ");
         }
         else {
         }

         if ((false && false)) {
            System.out.print(1000 + " ");
         }
         else {
         }

         if ((true || true)) {
            System.out.print(2 + " ");
         }
         else {
         }

         if ((true || false)) {
            System.out.print(3 + " ");
         }
         else {
         }

         if ((false || true)) {
            System.out.print(4 + " ");
         }
         else {
         }

         if ((false || false)) {
            System.out.print(1000 + " ");
         }
         else {
         }

         if ((!false)) {
            System.out.print(5 + " ");
         }
         else {
         }

         if ((!true)) {
            System.out.print(1000 + " ");
         }
         else {
         }

         if ((true || (true && false))) {
            System.out.print(6 + " ");
         }
         else {
         }
      }
   }

   private static class Program {
      public void run () {
         A a;
         System.out.println("6 1 2 3 4 5 6");
         a = new A();
         a.m();
      }
   }

}
