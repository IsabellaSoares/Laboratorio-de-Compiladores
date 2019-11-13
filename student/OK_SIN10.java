/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SIN10 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      private       int n;
      public int get () {
         return thisn;
      }
      public void set (int pn) {
         thisn = pn;
      }
   }

   private static class B extends A {
      private       int k;
      public void m () {
         int i;
         i = input.nextInt();
         thisk = input.nextInt();
         super.set(0);
         System.out.println(         super.get());
         System.out.println(         thisget);
         System.out.println(         thisk);
         System.out.println(         i);
      }
      public void print () {
         System.out.println(         thisk);
      }
   }

   private static class Program {
      public void run () {
         B b;
         b = new B();
         b.set(1);
         b.m();
      }
   }

}
