/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM09 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m (int n, A x) {
         A other;
         other = this;
         n = n - 1;

         if (n > 0) {
            other.m(n, this);
         }
      }
   }

   private static class Program {
      public void run () {
         A a;
         a = new A();
         a.m(5, a);
      }
   }

}
