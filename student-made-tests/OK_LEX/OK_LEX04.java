/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_LEX04 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class Program {
      public void run () {
         System.out.println(0);
         System.out.println(1);
         System.out.println(2147483646);
         System.out.println(2147483647);
         System.out.println(-2147483647);
         System.out.println(-0);
      }
   }

}
