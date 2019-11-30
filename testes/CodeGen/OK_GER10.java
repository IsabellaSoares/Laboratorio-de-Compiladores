/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER10 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      int k;
      public void m1 (int n) {
         this.k = 1;
         System.out.print(this.k + " " + n + " ");
      }
      public int getK () {
         return this.k;
      }
   }

   private static class B extends A {
      int k;
      public void m2 (int n) {
         this.k = 2;
         super.m1(1);
         System.out.print(this.k + " " + n + " ");
      }
      @Override public int getK () {
         return this.k;
      }
   }

   private static class C extends B {
      public void m3 (int n) {
         super.m2(2);
         System.out.print("3 " + n + " ");
      }
      public void m4 (int n) {
         this.m3(3);
         System.out.print("4 " + n + " ");
      }
   }

   private static class Program {
      public void run () {
         C c;
         System.out.println("1 1 2 2 3 3 4 4");
         c = new C();
         c.m4(4);
      }
   }

}
