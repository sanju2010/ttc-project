package fr.univnantes.lina.metrics;

public class MutualInformation implements AssociationRate {

	@Override
	public double getValue(int a,int b,int c,int d) {
		double x = ((double) a * (a + b+ c + d)) / ((double) ((a + b) * (a + c)));
		double y = Math.log(x);
		return y;
		/*
		System.out.println("a=" + a + " b=" + b + " c=" + c + " x=" + x + " y=" + y); 
		if (x == 0.0) {
			return x;
		} else {
			if (Double.isInfinite(y) || Double.isNaN(y)) {
				return 0.0;
			} else {
				return y;
			}
		}
		*/
	}

}
