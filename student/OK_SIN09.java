/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SIN09 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      private       int n;
      private       int k;
      private int m1 () {
         return thisn + 1;
      }
      private void m2 (int pk) {
         thisk = pk;
      }
      public int m () {
         this.m2(0);
         return thism1 + thisk;
      }
      public void init () {
         thisk = 1;
         thisn = 0;
      }
   }

   private static class Program {
      public void run () {
         A a;
         a = new A();
         System.out.println(         a.m());
      }
   }

}
