/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM13 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public void m () {
      }
      public void p () {
         int m;
      }
   }

   private static class Program {
      public void run () {
         A a;
         a = new A();
         a.p();
      }
   }

}
