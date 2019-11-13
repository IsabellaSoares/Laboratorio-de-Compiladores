/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM08 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      private       int i;
      public void put (int x, int y, boolean ok) {

         if ((x > y) &&          ok) {
            thisi = 0;
         }
      }
      public int get () {
         return thisi;
      }
      public void set (int i) {
         thisi = i;
      }
   }

   private static class B extends A {
      @Override public void put (int x, int y, boolean ok) {

         if (((x + y) < 1) && !ok) {
            System.out.print(0);
         }
      }
   }

   private static class Program {
      public void run () {
         B b;
         b = new B();
         b.put(1, 2, true);
      }
   }

}
