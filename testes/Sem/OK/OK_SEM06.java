/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM06 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      int n;
      public void set (int pn) {
         int n;
         this.n = pn;
      }
      public int put (int n, String set) {
         boolean put;
         this.n = n;
         return n;
      }
   }

   private static class Program {
      public void run () {
         A a;
         a = new A();
         a.set(0);
      }
   }

}
