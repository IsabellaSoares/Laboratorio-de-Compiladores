/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER11 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m1 () {
         System.out.print(" 2 ");
      }
      public void m2 (int n) {
         System.out.print(n + " ");
         this.m1;
      }
   }

   private static class B extends A {
      @Override void m1 () {
         System.out.println(" 4 ");
      }
   }

   private static class Program {
      public void run () {
         A a;
         B b;
         System.out.println("4 1 2 3 4");
         System.out.print("4 ");
         a = new A();
         a.m2(1);
         a = new B();
         a.m2(3);
      }
   }

}
