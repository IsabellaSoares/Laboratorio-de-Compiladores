/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER16 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      int k;
      public int get_A () {
         return this.k;
      }
      public void set (int k) {
         this.k = k;
      }
      public void print () {
         System.out.print(this.get_A() + " ");
      }
      public void init () {
         this.set(0);
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
      @Override public void print () {
         System.out.print(this.get_B() + " ");
         System.out.print(this.get_A + " ");
         super.print();
      }
   }

   private static class C extends A {
      @Override public int get_A () {
         return 0;
      }
   }

   private static class Program {
      public void run () {
         A a;
         B b;
         C c;
         System.out.println("2 2 0 0 2 0 0 0 0 0 0");
         b = new B();
         b.init();
         c = new C();
         c.init();
         System.out.print(b.get_B() + " ");
         a = b;
         a.print();
         b.print();
         a.init();
         b.init();
         System.out.print(a.get_A() + " ");
         System.out.print(b.get_A() + " ");
         a = c;
         System.out.print(a.get_A() + " ");
         c = new C();
         System.out.print(c.get_A() + " ");
      }
   }

}
