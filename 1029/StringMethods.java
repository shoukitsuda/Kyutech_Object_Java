
class StringMethods {

    int find(String s, String p) {
      int i = s.indexOf(p);
      if(i<0){
        return i;
      }
      else{
        return i+1;
      }
    }

    int count(String s, String p) {
      //以下失敗
      // int i = s.indexOf(p,0);
      // int j;
      // if(i<0){
      //   return i;
      // }else{
      //   int count = 1;
      //   for (j=i;j < -1 ;j =s.indexOf(p,j+1) ){
      //       count ++;
      //   }
      //   retun count;
      // }

      int cnt = 0;
      int i = s.indexOf(p);
      while(i>=0){
        cnt++;
        i = s.indexOf(p,i+1);
      }
      return cnt;
    }

    int countNOV(String s, String p) {
      int count = 0;

      for (int i = 0;s.length()-i>=p.length() ;i++ ) {
        if (s.substring(i,i+p.length()).equals(p)) {
          count++;
          i+=p.length()-1;
        }
      }
    return count;
    }

  String between(String s, char b, char e) {
      int beg = s.indexOf(b);
      int end  = s.indexOf(e,beg);
      if(beg<1||end<1){
        return "" ;
    }else{
          return s.substring(beg,end+1);
        }
      }

    String concat(String [] ss) {
      String result = "";
      String space =" ";

      for (int i= 0; i < ss.length; i++ ) {
        if (i!=ss.length -1){
          result = result.concat(ss[i]);
          result = result.concat(space);
        }else{
          result = result.concat(ss[i]);
        }
      }
      return result;
    }

    String reverse(String s) {
      char [] ss = s.toCharArray();
      String p = "";
      for (int i = s.length()-1;i>=0 ;i-- ) {
        p += ss[i];
      }
      return p;
    }

    public static void main(String [] args) {


        StringMethods sm = new StringMethods();

        System.out.println(sm.find("Hello World", "or"));
        System.out.println(sm.count("HoHoHo Ho", "HoHo"));
        System.out.println(sm.countNOV("HoHoHo Ho", "HoHo"));
        System.out.println(sm.between("Hello World", 'e', 'o'));
        System.out.println(sm.concat(new String [] { "Hello", "World" }));
        System.out.println(sm.reverse("Hello World"));

    }
}
