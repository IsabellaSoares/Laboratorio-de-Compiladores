/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class ER_SEM40 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      private       int n;
      public void set (int n) {
         thisn = n;
      }
      public int get () {
         return thisn;
      }
   }

   private static class B extends A {
      @Override void set (int pn) {
         System.out.println(         pn);
         super.set(pn);
      }
   }

   private static class Program {
      public void m (A b) {
         b.set(0);
      }
      public void run () {
         A a;
         a = new A();
         this.m(         a);
      }
   }

}
