/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM12 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
      }
   }

   private static class B extends A {
      public void p () {
         B A;
         A = new B();
         A.m();
      }
   }

   private static class Program {
      public void run () {
         A a;
         B b;
         a = new A();
         b = new B();
         a.m();
         b.p();
      }
   }

}
