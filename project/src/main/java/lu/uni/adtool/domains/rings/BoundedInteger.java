package lu.uni.adtool.domains.rings;

import java.io.Serializable;

/**
 * Class representing bounded integers.
 *
 * @author Piot Kordy
 */
public class BoundedInteger implements Serializable, Ring {

  /**
   * Constructs a new instance.
   *
   * @param value
   *          new value
   * @param bound
   *          upper bound on integers.
   */
  public BoundedInteger(final int value, final int bound) {
    this.value = value;
    this.bound = bound;
    checkValue();
  }

  public Object clone() {
    return new BoundedInteger(value, bound);
  }

  /**
   * Gets the value for this instance.
   *
   * @return The value.
   */
  public final int getValue() {
    return this.value;
  }

  /**
   * Gets the upper bound for this instance.
   *
   * @return The bound.
   */
  public final int getBound() {
    return this.bound;
  }

  /**
   * Sum with upper bound of two integers.
   *
   * @param a
   *          first bounded integer.
   * @param b
   *          second bounded integer.
   * @return sum or upper bound if sum is larger than the bound.
   */
  public static BoundedInteger sum(final BoundedInteger a, final BoundedInteger b) {
    if (a.getValue() == INF || b.getValue() == INF) {
      return new BoundedInteger(INF, Math.min(a.getBound(), b.getBound()));
    }
    return new BoundedInteger(a.getValue() + b.getValue(), Math.min(a.getBound(), b.getBound()));
  }

  /**
   * Max of two integers.
   *
   * @param a
   *          first bounded integer.
   * @param b
   *          second bounded integer.
   * @return bigger of the two integers.
   */
  public static BoundedInteger max(final BoundedInteger a, final BoundedInteger b) {
    if (a.getValue() == INF || b.getValue() == INF) {
      return new BoundedInteger(INF, Math.min(a.getBound(), b.getBound()));
    }
    return new BoundedInteger(Math.max(a.getValue(), b.getValue()),
        Math.min(a.getBound(), b.getBound()));
  }

  /**
   * Min of two integers.
   *
   * @param a
   *          first bounded integer.
   * @param b
   *          second bounded integer.
   * @return smaller of the two integers.
   */
  public static BoundedInteger min(final BoundedInteger a, final BoundedInteger b) {
    if (a.getValue() == INF) {
      return new BoundedInteger(b.getValue(), Math.min(a.getBound(), b.getBound()));
    }
    if (b.getValue() == INF) {
      return new BoundedInteger(a.getValue(), Math.min(a.getBound(), b.getBound()));
    }
    return new BoundedInteger(Math.min(a.getValue(), b.getValue()),
        Math.min(a.getBound(), b.getBound()));
  }

  /**
   * {@inheritDoc}
   *
   * @see Object#toString()
   */
  public final String toString() {
    if (getValue() == -1) {
      return "Infinity";
    }
    else {
      return new Integer(getValue()).toString();
    }
  }

  /**
   * Unicode representation of a number.
   *
   * @return string with unicode representation.
   */
  public final String toUnicode() {
    if (getValue() == -1) {
      return "\u221E";
    }
    else {
      return new Integer(getValue()).toString();
    }
  }

  /**
   * Set the new value reading from the string Returns false if the value is not
   * valid.
   *
   */
  public final boolean updateFromString(String s) {
    value = Integer.parseInt(s);
    return checkValue();
  }

  /**
   * Checks if the value is bigger than zero and lower than bound
   */
  private boolean checkValue() {
    if (value < -1) {
      value = 0;
      return false;
    }
    if (value > bound) {
      value = bound;
      return false;
    }
    return true;

  }

  public int compareTo(Object o) {
    if (o instanceof BoundedInteger) {
      int val2 = ((BoundedInteger) o).getValue();
      if (value == val2) {
        return 0;
      }
      if (value < val2) {
        return -1;
      }
      if (value > val2) {
        return 1;
      }
    }
    throw new ClassCastException("Unable to compare BoundedInteger class with " + o);
  }

  static final long serialVersionUID = 94244625469424462L;
  public static int INF              = -1;
  private int       value;
  private int       bound;
}