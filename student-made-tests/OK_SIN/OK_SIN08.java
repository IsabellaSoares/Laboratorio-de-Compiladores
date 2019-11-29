/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SIN08 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public int m (int x, int y, boolean ok) {
         return x + y;
      }
   }

   private static class Program {
      public void run () {
         A a;
         a = new A();
         System.out.println(a.m("null", "null", "null"));
      }
   }

}
