/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER14 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      int k;
      public int get_A () {
         return this.k;
      }
      public void init () {
         this.k = 1;
      }
   }

   private static class B extends A {
      int k;
      public int get_B () {
         return this.k;
      }
      @Override public void init () {
         super.init();
         this.k = 2;
      }
   }

   private static class C extends B {
      int k;
      public int get_C () {
         return this.k;
      }
      @Override public void init () {
         super.init();
         this.k = 3;
      }
   }

   private static class D extends C {
      int k;
      public int get_D () {
         return this.k;
      }
      @Override public void init () {
         super.init();
         this.k = 4;
      }
   }

   private static class Program {
      public void run () {
         A a;
         B b;
         C c;
         D d;
         System.out.println("4 3 2 1");
         d = new D();
         d.init();
         System.out.print(d.get_D() + " ");
         c = d;
         System.out.print(c.get_C() + " ");
         b = c;
         System.out.print(b.get_B() + " ");
         a = b;
         System.out.print(a.get_A() + " ");
      }
   }

}
