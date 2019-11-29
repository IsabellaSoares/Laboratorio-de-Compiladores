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
         return this.n;
      }
      public void set (int pn) {
         this.n = pn;
      }
   }

   private static class B extends A {
      private       int k;
      public void m () {
         int i;
         i = input.nextLine();
         this.k = input.nextLine();
         super.set("null");
         System.out.println(         super.get());
         System.out.println(         this.get);
         System.out.println(         this.k);
         System.out.println(         i);
      }
      public void print () {
         System.out.println(         this.k);
      }
   }

   private static class Program {
      public void run () {
         B b;
         b = new B();
         b.set("null");
         b.m();
      }
   }

}
