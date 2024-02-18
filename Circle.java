package model;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;

public class Circle<T> {
	public static final MathContext DEFAULT_PRECISION = MathContext.DECIMAL128;

    public final BigDecimal PI_VALUE;
    public final MathContext PRECISION;

    private BigDecimal radius;
    private BigDecimal centerX;
    private BigDecimal centerY;
    
    public Circle() {
    	this(DEFAULT_PRECISION);
    }
    public Circle(MathContext precision) {
    	PRECISION = (precision != null) ? precision : DEFAULT_PRECISION;
        PI_VALUE = BigDecimalMath.pi(PRECISION);
        
        radius = BigDecimal.ZERO;
        centerX = BigDecimal.ZERO;
        centerY = BigDecimal.ZERO;
    }

    public BigDecimal getCenterX() {
		return centerX;
	}
	public void setCenterX(T value) {
		this.centerX = convertToBigDecimal(value);
	}

	public BigDecimal getCenterY() {
		return centerY;
	}
	public void setCenterY(T value) {
		this.centerY = convertToBigDecimal(value);
	}
	
	public void setCoordinates(T centerX, T centerY) {
		setCenterX(centerX);
		setCenterY(centerY);
	}

	public BigDecimal getRadius() {
        return radius;
    }
    public void setRadius(T value) {
    	BigDecimal radius = convertToBigDecimal(value);
        checkNonNegative(radius);

        this.radius = radius;
    }

    public BigDecimal getDiameter() {
        return this.radius.multiply(BigDecimal.valueOf(2));
    }
    public void setDiameter(T value) {
    	BigDecimal diameter = convertToBigDecimal(value);
        checkNonNegative(diameter);
        
        this.radius = diameter.divide(BigDecimal.valueOf(2), PRECISION);
    }

    public BigDecimal getArea() {
        return PI_VALUE.multiply(this.radius.pow(2));
    }
    public void setArea(T value) {
    	BigDecimal area = convertToBigDecimal(value);
        checkNonNegative(area);
        
        this.radius = BigDecimalMath.sqrt(area.divide(PI_VALUE, PRECISION), PRECISION);
    }

    public BigDecimal getCircumference() {
        return PI_VALUE.multiply(this.radius).multiply(BigDecimal.valueOf(2));
    }
    public void setCircumference(T value) {
    	BigDecimal circumference = convertToBigDecimal(value);
        checkNonNegative(circumference);

        this.radius = circumference.divide(PI_VALUE.multiply(BigDecimal.valueOf(2)), PRECISION);
    }

    public BigDecimal centerDistanceToPoint(T pointX, T pointY) {
    	BigDecimal x = convertToBigDecimal(pointX);
    	BigDecimal y = convertToBigDecimal(pointY);
    	
        BigDecimal distance = BigDecimalMath.sqrt(
            x.subtract(centerX).pow(2).add(y.subtract(centerY).pow(2)),
            PRECISION
        );
        
        return distance;
    }
    public BigDecimal distanceToPoint(T pointX, T pointY) {
    	return centerDistanceToPoint(pointX, pointY).subtract(radius);
    }
    public byte comparePointToCircle(T pointX, T pointY) {
        return (byte) centerDistanceToPoint(pointX, pointY).compareTo(this.radius);
    }
    
    private BigDecimal convertToBigDecimal(T value) {
        if (value instanceof Number) {
            return new BigDecimal(((Number) value).toString(), PRECISION);
        } else if (value instanceof String) {
            return new BigDecimal((String) value, PRECISION);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + value.getClass());
        }
    }

    private void checkNonNegative(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }
    }
}

