/*
Isabella Soares de Lima             726541
Marcelo Augusto Rodrigues da Silva  726565
*/

import java.util.*;

public class OK_SIN14 {
   public static Scanner input = new Scanner(System.in);

   public static void main(String []args) {
      new Program().run();
   }

   private static class Point {
      int x;
      int y;
      public void set (int x, int y) {
         this.x = x;
         this.y = y;
      }
      public int getX () {
         return this.x;
      }
      public int getY () {
         return this.y;
      }
   }

   private static class Program {
      Point p;
      public void run () {
         this.p = new Point();
         this.p(00);
      }
      public void getPoint () {
         return this.p;
      }
   }

}
