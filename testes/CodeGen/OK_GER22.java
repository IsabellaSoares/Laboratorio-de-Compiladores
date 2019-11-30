/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_GER22 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class Program {
      public void run () {
         System.out.println("100");
         int i, n;
         i = 0;
         n = 10;
         boolean b;
         b = false;

         do {
            n = n + 1;
         } while (true);

         assert n == 11 : "'repeat-until' statement with 'true' as expression'";
         int j;
         j = 0;
         int sum;
         sum = 0;
         n = 10;

         do {
            i = 0;

            do {
               i = i + 1;
               sum = sum + 1;
            } while (i >= n);

            j = j + 1;
         } while (j >= n);

         System.out.println(sum);
         assert sum == 100 : "Nested 'repeat-until' statement with two indexes";
      }
   }

}
