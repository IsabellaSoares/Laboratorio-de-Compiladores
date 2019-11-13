/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_CALC {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class A {
      public int calculaNota (int ntrab, int nprov) {
         int ans;
         ans = 11;

         if (ntrab >= 6) {

            if (nprov >= 6) {
               ans = ntrab + nprov;
               ans = ans / 2;
               return ans;
            }
         }
         return ans;
      }
      public int calculaNotaB (int ntrab, int nprov) {
         int ans;
         ans = ((2 * ntrab) + nprov) / 3;
         return ans;
      }
   }

   private static class B extends A {
      @Override int calculaNotaB (int ntrab, int nprov) {

         if (ntrab < nprov) {
            return ntrab;
         }
      }
   }

   private static class Program {
      public void run () {
         B a;
         int ans;
         int ntrab;
         int nprov;
         System.out.println("");
         System.out.println("Ok-fib");
         System.out.println("The output should be :");
         System.out.println("0 <= x <= 10");
         a = new A();
         nprov = input.nextInt();
         ntrab = input.nextInt();
         ans = a.calculaNota(ntrab, nprov);

         if (ans != 11) {
            System.out.println(            ans);
         }
      }
   }

}
