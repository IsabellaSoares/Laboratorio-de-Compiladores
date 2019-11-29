/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SEM17 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class Program {
      public void run () {
         int n = input.nextLine();

         if (n < 15) {
            System.out.println("pequeno");
            assert n < 15 : "n < 15";
         }
         else {
            System.out.println("grande");
            assert n >= 15 : "n >= 15";
         }
      }
   }

}
