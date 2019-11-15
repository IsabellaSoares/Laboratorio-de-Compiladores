/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM04 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class Program {
      public void run () {
         int a, b;
         boolean e, f;
         a = 1;
         b = 1;
         e = true;
         f = false;

         if ((a == b) && (a == 1) && (1 == b) && (a != b) && (e == f) && (true == f) && (e != f) && (true != f)) {
            System.out.println("impossivel");
         }
         else {
         }
      }
   }

}
