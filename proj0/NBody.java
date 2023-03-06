
public class NBody {
	public static double readRadius(String fileName) {
		In in = new In(fileName);
		int number = in.readInt();
		double radius = in.readDouble();
		return radius;
	}

	public static Planet[] readPlanets(String fileName) {
		In in = new In(fileName);
		int number = in.readInt();
		double radius = in.readDouble();
		Planet[] planets= new Planet[number];
		for(int i = 0; i< number; i++) {
			double xx = in.readDouble();
			double yy = in.readDouble();
			double vx = in.readDouble();
			double vy = in.readDouble();
			double mass = in.readDouble();
			String img = in.readString();
			planets[i]=new Planet(xx, yy, vx, vy, mass, img);
		}
		return planets;
	}
	
	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double r = readRadius(filename);
		Planet[] planets = readPlanets(filename);
		
		StdDraw.setXscale(-r, r);
		StdDraw.setYscale(-r, r);
		StdDraw.enableDoubleBuffering();
		
		double t = 0;
		int number = planets.length;
		while(t < T) {
			double[] xForces = new double[number];
			double[] yForces = new double[number];
			
			for(int i = 0; i< number; i++) {
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
			}
			
			for(int i = 0; i< number; i++) {
				planets[i].update(dt, xForces[i], yForces[i]);
			}
			
			StdDraw.picture(0, 0, "images/starfield.jpg");
			
			for(Planet planet : planets) {
				planet.draw();
			}
			
			StdDraw.show();
			StdDraw.pause(10);
			t += dt;
		}
		
		StdOut.printf("%d\n", planets.length)
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) {
		    StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
		                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName); 
	}
}
