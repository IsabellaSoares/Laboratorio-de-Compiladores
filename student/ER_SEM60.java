/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class ER_SEM60 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      private void p () {
      }
      public void m () {
         thisp;
      }
   }

   private static class B extends A {
      @Override void m () {
         super.p();
      }
   }

   private static class Program {
      public void run () {
         B b;
         b = new B();
         b.m();
      }
   }

}
