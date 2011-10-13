package fr.univnantes.lina.metrics;

public class LogLikelihood implements AssociationRate {

	private double getLogLike(double x) {
		return x * Math.log(x);
	}
	
	@Override
	public double getValue(int a,int b,int c,int d) {
		return 
			this.getLogLike(a) 
			+ (b <= 0 ? 0.0 : this.getLogLike(b))
			+ (c <= 0 ? 0.0 : this.getLogLike(c))
			+ this.getLogLike(d)
			+ this.getLogLike(a + b + c + d)
			- this.getLogLike(a + b)
			- this.getLogLike(a + c)
			- this.getLogLike(b + d)
			- this.getLogLike(c + d)
			;
	}
	
}
