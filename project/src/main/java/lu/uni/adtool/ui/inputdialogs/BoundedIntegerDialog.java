package lu.uni.adtool.ui.inputdialogs;

import lu.uni.adtool.domains.rings.BoundedInteger;
import lu.uni.adtool.tools.Options;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;

/**
 * Dialog to edit values for the attribute domains of real numbers greater than
 * 0..
 *
 * @author Piot Kordy
 */
public class BoundedIntegerDialog extends InputDialog {

  /**
   * Constructs a new instance.
   *
   * @param frame
   *          parent frame.
   */
  public BoundedIntegerDialog(final Frame frame) {
    super(frame, Options.getMsg("inputdialog.intnumber.txt"));
    infButton = null;
  }

  /**
   * Constructs a new instance.
   *
   * @param frame
   *          parent frame.
   * @param title
   *          window title.
   */
  public BoundedIntegerDialog(final Frame frame, final String title) {
    super(frame, title);
  }

  /**
   * Handle clicks on the various buttons. {@inheritDoc}
   *
   * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
   */
  public void actionPerformed(final ActionEvent e) {
    if ("-100".equals(e.getActionCommand())) {
      add(-100);
    }
    else if ("-10".equals(e.getActionCommand())) {
      add(-10);
    }
    else if ("-1".equals(e.getActionCommand())) {
      add(-1);
    }
    else if ("+1".equals(e.getActionCommand())) {
      add(1);
    }
    else if ("+10".equals(e.getActionCommand())) {
      add(10);
    }
    else if ("+100".equals(e.getActionCommand())) {
      add(100);
    }
    else if ("/1000".equals(e.getActionCommand())) {
      divide(1000);
    }
    else if ("/100".equals(e.getActionCommand())) {
      divide(100);
    }
    else if ("/10".equals(e.getActionCommand())) {
      divide(10);
    }
    else if ("x10".equals(e.getActionCommand())) {
      times(10);
    }
    else if ("x100".equals(e.getActionCommand())) {
      times(100);
    }
    else if ("x1000".equals(e.getActionCommand())) {
      times(1000);
    }
    else if ("Infinity".equals(e.getActionCommand())) {
      valueField.setValue(new Double(Double.POSITIVE_INFINITY));
      sync();
    }
    else if ("Zero".equals(e.getActionCommand())) {
      valueField.setValue(new Double(0));
      sync();
    }
    else {
      super.actionPerformed(e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see InputDialog#isValid(double)
   */
  protected boolean isValid(final double d) {
    boolean isValid = false;
    final int bound = ((BoundedInteger) value).getBound();
    if (new Double(d).isInfinite()) {
      isValid = false;
    }
    else if (d >= 0 && d <= bound) {
      isValid = true;
    }
    return isValid;
  }

  /**
   * {@inheritDoc}
   *
   * @see InputDialog#setValue(double)
   */
  protected final void setValue(final double d) {
    final int bound = ((BoundedInteger) value).getBound();
    if (new Double(d).isInfinite()) {
      value = new BoundedInteger(BoundedInteger.INF, bound);
    }
    else {
      value = new BoundedInteger((int) d, bound);
    }
    valueField.setValue(d);
  }
  /**
   * {@inheritDoc}
   *
   * @see InputDialog#sync()
   */
  // protected final boolean sync()
  // {
  // if (isV
  // final Number num = (Number)valueField.getValue();
  // if(num == null){
  // error=true;
  // }
  // else{
  // final double d = num.doubleValue();
  // final int bound = ((BoundedInteger)value).getBound();
  // if(new Double(d).isInfinite()){
  // value = new BoundedInteger(BoundedInteger.INF,bound);
  // valueField.setValue(value);
  // }
  // else if(d >= 0 && d<=bound){
  // value = new BoundedInteger((int)d,bound);
  // valueField.setValue(((BoundedInteger)value).getValue());
  // }
  // else{
  // error = true;
  // valueField.setValue(new Integer(((BoundedInteger)value).getValue()));
  // }
  // }
  // return error;
  // }

  /**
   * {@inheritDoc}
   *
   * @see InputDialog#createLayout()
   */
  protected void createLayout(final boolean showDefault) {
    final int bound = ((BoundedInteger) value).getBound();
    errorMsg.setText(Options.getMsg("inputdialog.intnumber.error") + " " + bound + ".");
    final NumberFormat f = NumberFormat.getInstance();
    f.setParseIntegerOnly(true);
    valueField = new JFormattedTextField(f);
    valueField.addKeyListener(this);
    double d = new Double(((BoundedInteger) value).getValue());
    if (d == BoundedInteger.INF) {
      d = Double.POSITIVE_INFINITY;
    }
    if (showDefault) {
      valueField.setValue(d);
    }
    valueField.setColumns(15);
    valueField.addPropertyChangeListener("value", this);
    final JPanel inputPane = new JPanel();
    inputPane.setLayout(new GridBagLayout());
    final GridBagConstraints c = new GridBagConstraints();
    JButton button;
    c.insets = new Insets(0, 8, 0, 0);
    c.gridy = 0;
    c.gridx = 0;
    button = new JButton("-100");
    button.setActionCommand("-100");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 1;
    button = new JButton("-10");
    button.setActionCommand("-10");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 2;
    button = new JButton("-1");
    button.setActionCommand("-1");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 3;
    c.gridwidth = 2;
    inputPane.add(valueField, c);
    c.gridwidth = 1;
    c.gridx = 5;
    button = new JButton("+1");
    button.setActionCommand("+1");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 6;
    button = new JButton("+10");
    button.setActionCommand("+10");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 7;
    button = new JButton("+100");
    button.setActionCommand("+100");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridy = 1;
    c.gridx = 0;
    button = new JButton("/1000");
    button.setActionCommand("/1000");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 1;
    button = new JButton("/100");
    button.setActionCommand("/100");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 2;
    button = new JButton("/10");
    button.setActionCommand("/10");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 3;
    button = new JButton(Options.getMsg("inputdialog.zero"));
    button.setActionCommand(Options.getMsg("inputdialog.zero"));
    button.addActionListener(this);
    if (infButton != null) {
      inputPane.add(button, c);
      c.gridx = 4;
      infButton.setActionCommand(Options.getMsg("inputdialog.infinity"));
      infButton.addActionListener(this);
      inputPane.add(infButton, c);
    }
    else {
      c.gridwidth = 2;
      inputPane.add(button, c);
      c.gridwidth = 1;
    }
    c.gridx = 5;
    button = new JButton("x10");
    button.setActionCommand("x10");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 6;
    button = new JButton("x100");
    button.setActionCommand("x100");
    button.addActionListener(this);
    inputPane.add(button, c);
    c.gridx = 7;
    button = new JButton("x1000");
    button.setActionCommand("x1000");
    button.addActionListener(this);
    inputPane.add(button, c);
    contentPane.add(inputPane, BorderLayout.CENTER);
    pack();
  }

  private void add(final int i) {
    final Number num = (Number) valueField.getValue();
    if (num == null) {
      return;
    }
    double d = num.doubleValue();
    if (!isValid(d + i) || d == Double.POSITIVE_INFINITY) {
      return;
    }
    d = d + i;
    setValue(d);
  }

  private void times(final int i) {
    final Number num = (Number) valueField.getValue();
    if (num == null) {
      return;
    }
    double d = num.doubleValue();
    if (!isValid(d * i) || d == Double.POSITIVE_INFINITY) {
      return;
    }
    d = d * i;
    setValue(d);
  }

  private void divide(final int i) {
    final Number num = (Number) valueField.getValue();
    if (num == null) {
      return;
    }
    double d = num.doubleValue();
    if (!isValid((int) d / i) || d == Double.POSITIVE_INFINITY) {
      return;
    }
    d = (int) d / i;
    setValue(d);
  }
  static final long serialVersionUID = 35393957497521213L;
  protected JButton infButton;
}