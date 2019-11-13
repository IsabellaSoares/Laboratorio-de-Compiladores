/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class ER_SEM51 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      private       int i;
      public void put (int x, int y, boolean ok) {
      }
   }

   private static class B extends A {
      public void put (int x, int y, int ok) {
      }
   }

   private static class Program {
      public void run () {
         A a;
         a = new A();
         a.put(0, 1, true);
      }
   }

}
