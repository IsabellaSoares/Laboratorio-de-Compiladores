/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM05 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      private       int n;
      public void set (int pn) {
         this.n = pn;
      }
      public int get () {
         return this.n;
      }
   }

   private static class B extends A {
      @Override public void set (int pn) {
         System.out.print(pn);
         super.set(pn);
      }
   }

   private static class Program {
      public void m (A a) {
         a.set(0);
         return new B();
      }
      public void p (int i) {

         if ((i > 0)) {
            return new A();
         }
         else {
            return new B();
         }
      }
      public void run () {
         A a;
         B b;
         a = new A();
         b = new B();
         a = b;
         a.set(0);
         a = this.m(a);
         b = this.m(b);
         a = this.p(0);
      }
   }

}
