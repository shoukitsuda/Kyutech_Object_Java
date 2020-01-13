class Point2D {
    double x, y;
    Point2D() { }  // to be used implicitly by subclasses
    Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    Point2D(Point2D p) { //
        this.x = p.getX();
        this.y = p.getY();
    }
    Point2D move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        return this;
    }
    Point2D move(Point2D p) { //
        return move(p.getX(), p.getY());
    }
    Point2D diff(Point2D p) { //
        return move(-p.getX(), -p.getY());
    }
    Point2D mul(double a) { //
        this.x *= a;
        this.y *= a;
        return this;
    }
    double dist(Point2D v) {
        double dx = this.getX() - v.getX();
        double dy = this.getY() - v.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    double dist() {
        double dx = this.getX();
        double dy = this.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    double getX() {
        return x;
    }
    double getY() {
        return y;
    }
    Point2D rotate(double ang) {
        ang = ang / 360. * 2 * Math.PI; // deg -> rad
        double r = this.dist();
        ang += Math.atan2(y, x);
        this.x = r * Math.cos(ang);
        this.y = r * Math.sin(ang);
        return this;
    }
    Point2D rotate(Point2D v, double ang) {
        double vx = v.getX();
        double vy = v.getY();
        this.move(-vx, -vy).rotate(ang).move(vx, vy);
        return this;
    }
    String show() {
        return "(" + getX() + ", " + getY() + ")";
    }
}

//
class Point2DPolar extends Point2D {
    double r, theta;
    Point2DPolar(double x, double y) {
        theta = Math.atan2(y, x);
        r = Math.sqrt(x * x + y * y);
    }
    Point2DPolar(Point2D p) { //
        this(p.getX(), p.getY());  //
    }
    Point2D move(double dx, double dy) {
        double x = getX() + dx;
        double y = getY() + dy;
        theta = Math.atan2(y, x);
        r = Math.sqrt(x * x + y * y);
        return this;
    }
    Point2D mul(double a) { //
        this.r *= a;
        return this;
    }
    double dist() {
        return r;
    }
    double getX() {
        return r * Math.cos(theta);
    }
    double getY() {
        return r * Math.sin(theta);
    }
    Point2D rotate(double ang) {
        ang = ang / 360. * 2 * Math.PI; // deg -> rad
        theta += ang;
        return this;
    }
}

//
class Poly {
    Point2D [] ps;   //
    Poly(Point2D [] ps) {
        this.ps = ps;
    }

    Point2D center() { //　
         double sum_x=0;
         double sum_y=0;

         for (Point2D i :ps){
             sum_x += i.gerX();
             sum_y += i.gerY();
         }
         double center_x = sum_x/ps.length;
         double center_y = sum_y/ps.length;
         return new Point2D (center_x,center_y);
    }

    double area() { //
        double a = 0;
        for(int i = 0; i < ps.length; i++) {
            Point2D p1 = ps[i];
            Point2D p2 = ps[(i+1)%ps.length];
            //
            a += p1.getX() * p2.getY() - p1.getY() * p2.getX();
        }
        return a/2;  //
    }
    String show() {
        return "Poly: " + showPoints();
    }
    String showPoints() {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < ps.length; i++) {
            sb.append(ps[i].show());
            sb.append("--");
        }
        sb.append("cycle");
        return sb.toString();
    }
    Poly rotateR() { //
        return rotate(-90);
    }
    Poly rotateL() { //
        return rotate(90);
    }
    Poly rotate(double ang) { //
      ang = ang / 360. * 2 * Math.PI;
      Point2D c = ps.center();
      double r = this.dist();
      ang += Math.atan2(y, x);  //((x,y))
      this.x = r * Math.cos(ang);
      this.y = r * Math.sin(ang);

      new Point2D (new_x,new_y);
      return this;
    }

    Poly move(double dx, double dy) { //
      this.x += dx;
      this.y += dy;
      return this;
    }
}

// define Triangle class here.
class Triangle extends Poly{
Triangle(Point2D p1, Point2D p2, Point2D p3){
  super(new Point2D [] {p1,p2,p3});
}
String show(){
  return "Triangle:" * showPoints();
 }
}
// define Rectangle class here.
class Rectangle extends Poly{
  static Point2D[] genPoints(Point2D p1,Point2D p2,double h){
    Point2D v = new Point2D(p1).diff(p２);
    Point2D w = new Point2D(-v.getY(),v.getX());
    w.mul(h/w.dist());
    Point2D p3 = new Point2D(p2).move(w);
    Point2D p4 = new Point2D(p1).move(w);
    return new Point2D[]{p1, p2, p3, p4};
  }
  Rectangle(Point2D p1,Point2D p2,double h){
    super(genPoints(p1,p2,h));
  }
  String show(){
    return "Rectangle:" + showPoints();
  }
 }

// define Square class here.
class Square extends Rectangle{
  Square(Point2D p1,Point2D p2){
    super(p1,p2,new Point2D(p1).diff(p2).dist());
  }
  String show(){
    return "Square:"+ showPoints();
  }
}

class PolyMain {
    public static void main(String [] args) {
        Rectangle [] rs = new Rectangle[2];
        rs[0] = new Rectangle(new Point2D(0,0),
                              new Point2D(2,2),
                              Math.sqrt(2)*4);
        rs[1] = new Square(new Point2D(2,0),
                           new Point2D(2,2));
        System.out.println("---- Rectangle array ----");
        for(Rectangle r : rs) {
            System.out.println(r.show());
        }
        Poly [] ps = new Poly[6];
        ps[0] = rs[0];
        ps[1] = rs[1];
        ps[2] = new Triangle(new Point2D(3,0),
                             new Point2D(0,0),
                             new Point2D(-1.5,-2));
        ps[3] = new Poly(new Point2D []{
                new Point2D(8,4),
                new Point2D(10,4),
                new Point2D(11,5),
                new Point2D(10,6),
                new Point2D(8,6)
            });
        // the same as ps[1] but by Poly
        ps[4] = new Poly(new Point2D []{
                new Point2D(3,0),
                new Point2D(3,2),
                new Point2D(1,2),
                new Point2D(1,0)
            });
        //
        ps[5] = new Poly(new Point2D []{
                new Point2D(3,0),
                new Point2DPolar(3,2),
                new Point2D(1,2),
                new Point2DPolar(1,0)
            });

        System.out.println("---- Poly array ----");
        for(Poly p : ps) {
            System.out.println(p.show());
        }
        System.out.println("---- doing move ----");
        for(Poly p : ps) {
            p.move(3,0);
        }
        for(Poly p : ps) {
            System.out.println(p.show());
        }
        System.out.println("---- doing RotateR ----");
        for(Poly p : ps) {
            p.rotateR();
        }
        for(Poly p : ps) {
            System.out.println(p.show());
        }
        // Square has no error while others have some
    }
}
