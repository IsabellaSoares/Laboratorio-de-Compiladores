/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM15 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void print () {
         System.out.println(0);
      }
      public void accept (B x) {
         x.print();
      }
   }

   private static class B extends A {
      public void m () {
         super.accept(this.self);
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
