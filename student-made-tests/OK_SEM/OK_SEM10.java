/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM10 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void first (int pn) {
      }
   }

   private static class B extends A {
      public void second () {
      }
   }

   private static class C extends B {
      public void third () {
      }
   }

   private static class Program {
      public void run () {
         A a;
         B b;
         C c;
         a = new A();
         b = new B();
         c = new C();
         a.first(0);
         b.first(0);
         c.first(0);
         b.second();
         c.second();
         c.third();
         a = b;
         a = c;
         b = c;
      }
   }

}
