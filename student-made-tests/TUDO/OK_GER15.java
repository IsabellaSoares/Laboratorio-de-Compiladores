/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER15 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      int i;
      int j;
      private void p () {
         System.out.print(this.i + " ");
      }
      private void q () {
         System.out.print(this.j + " ");
      }
      public void init_A () {
         this.i = 1;
         this.j = 2;
      }
      public void call_p () {
         this.p();
      }
      public void call_q () {
         this.q();
      }
      public void r () {
         System.out.print(this.i + " ");
      }
      public void s () {
         System.out.print(this.j + " ");
      }
   }

   private static class B extends A {
      int i;
      int j;
      private void p () {
         System.out.print(this.i + " ");
      }
      private void q () {
         System.out.print(this.j + " ");
      }
      public void init_B () {
         this.i = 3;
         this.j = 4;
      }
      @Override public void call_p () {
         this.p();
      }
      @Override public void call_q () {
         this.q();
      }
      @Override public void r () {
         System.out.print(this.i + " ");
      }
      @Override public void s () {
         System.out.print(this.j + " ");
      }
   }

   private static class C extends A {
      int i;
      int j;
      private void p () {
         System.out.print(this.i + " ");
      }
      private void q () {
         System.out.print(this.j + " ");
      }
      public void init_C () {
         this.i = 5;
         this.j = 6;
      }
      @Override public void call_p () {
         this.p();
      }
      @Override public void call_q () {
         this.q();
      }
      @Override public void r () {
         System.out.print(this.i + " ");
      }
      @Override public void s () {
         System.out.print(this.j + " ");
      }
   }

   private static class Program {
      public void run () {
         A a;
         B b;
         C c;
         System.out.println("1 2 1 2 3 4 3 4 5 6 5 6");
         a = new A();
         a.init_A();
         a.call_p();
         a.call_q();
         a.r();
         a.s();
         b = new B();
         b.init_B();
         b.init_A();
         b.call_p();
         b.call_q();
         b.r();
         b.s();
         c = new C();
         c.init_C();
         c.init_A();
         c.init_C();
         c.call_p();
         c.call_q();
         c.r();
         c.s();
      }
   }

}
