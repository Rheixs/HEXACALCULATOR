/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package planb;

import java.math.BigDecimal;

/**
 *
 * @author rhee
 */
public class CalculatorMidterm extends javax.swing.JFrame {

    double number, ans;
  int operators;
  private boolean operatorTyped = false;
  private boolean decimalAdded = false; 
  private boolean operate = false;
    public CalculatorMidterm() {
        initComponents();
        A.setEnabled(false);
        B.setEnabled(false);
        C.setEnabled(false);
        D.setEnabled(false);
        E.setEnabled(false);
        F.setEnabled(false);
        c.setSelected(true);
    }
    public void calculate() {
        String expression = InputOutput.getText();
        try {
            double result = evaluateExpression(expression);
            String formattedResult;
    
            if (result == (int) result) {
                formattedResult = String.format("%d", (int) result);
                decimalAdded = false;
            } else {
                formattedResult = String.format("%.2f", result);
                decimalAdded = true;
            }
    
            InputOutput.setText(formattedResult);
        } catch (Exception ex) {
            InputOutput.setText("");
        }
        operate = false;
    }
    
    
    public void hexMinus() {
        String input = InputOutput.getText();
        String[] parts = input.split("-");
    
        if (parts.length == 2) {
            String hex1 = parts[0].trim();
            String hex2 = parts[1].trim();
    
            if (isValidHexadecimalWithDecimal(hex1) && isValidHexadecimalWithDecimal(hex2)) {
                String resultHex = performHexadecimalSubtraction(hex1, hex2);
                InputOutput.setText(resultHex);
            } else {
                InputOutput.setText("Invalid hexadecimal input with a decimal point.");
            }
        } else {
            InputOutput.setText("Invalid input format. Please use 'hex1 - hex2' format.");
        }
    }
    
    private String performHexadecimalSubtraction(String hex1, String hex2) {
        double decimal1 = hexToDecimalWithDecimal(hex1);
        double decimal2 = hexToDecimalWithDecimal(hex2);
        double difference = decimal1 - decimal2;
    
        // Convert the difference back to hexadecimal with uppercase letters and no trailing zeros
        String resultHex = decimalToHexWithDecimal(difference);
    
        return resultHex;
    }
    

// Check if a string is a valid hexadecimal number with a decimal point
private boolean isValidHexadecimalWithDecimal(String input) {
    return input.matches("^[0-9A-Fa-f]+(\\.[0-9A-Fa-f]+)?$");
}

// Convert a hexadecimal number with a decimal point to its decimal representation
private double hexToDecimalWithDecimal(String hex) {
    hex = hex.toUpperCase();  // Convert the input to uppercase

    if (hex.contains(".")) {
        String[] parts = hex.split("\\.");
        int integerPart = Integer.parseInt(parts[0], 16);
        double fractionalPart = 0.0;

        if (parts.length > 1) {
            String fractionalHex = parts[1];
            for (int i = 0; i < fractionalHex.length(); i++) {
                char digit = fractionalHex.charAt(i);
                int digitValue = Character.digit(digit, 16);
                fractionalPart += digitValue / Math.pow(16, i + 1);
            }
        }

        return integerPart + fractionalPart;
    } else {
        return Integer.parseInt(hex, 16);
    }
}


private String decimalToHexWithDecimal(double decimal) {
    BigDecimal decimalValue = new BigDecimal(decimal);
    String[] parts = decimalValue.toPlainString().split("\\.");
    
    int integerPart = Integer.parseInt(parts[0]);
    String hexIntegerPart = Integer.toHexString(integerPart).toUpperCase();
    
    String hexFractionalPart = "";
    if (parts.length > 1) {
        String fractionalPart = parts[1];
        BigDecimal fractionalValue = new BigDecimal("0." + fractionalPart);
        
        for (int i = 0; i < 15; i++) {
            fractionalValue = fractionalValue.multiply(BigDecimal.valueOf(16));
            int digit = fractionalValue.intValue();
            hexFractionalPart += Integer.toHexString(digit).toUpperCase();
            fractionalValue = fractionalValue.subtract(BigDecimal.valueOf(digit));
        }

        // Remove trailing zeros
        hexFractionalPart = hexFractionalPart.replaceAll("0*$", "");
    }

    if (hexFractionalPart.isEmpty()) {
        return hexIntegerPart;
    } else {
        return hexIntegerPart + "." + hexFractionalPart;
    }
}


private static double evaluateExpression(String expression) {
        try {
            return new Object() {
                int pos = -1, ch;

                void nextChar() {
                    ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
                }

                boolean eat(int charToEat) {
                    while (Character.isWhitespace(ch)) nextChar();
                    if (ch == charToEat) {
                        nextChar();
                        return true;
                    }
                    return false;
                }

                double parse() {
                    nextChar();
                    double x = parseExpression();
                    if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                    return x;
                }

                double parseExpression() {
                    double x = parseTerm();
                    for (; ; ) {
                        if (eat('+')) x += parseTerm(); // addition
                        else if (eat('-')) x -= parseTerm(); // subtraction
                        else return x;
                    }
                }

                double parseTerm() {
                    double x = parseFactor();
                    for (; ; ) {
                        if (eat('*')) x *= parseFactor(); // multiplication
                        else if (eat('÷')) x /= parseFactor(); // division
                        else return x;
                    }
                }

                double parseFactor() {
                    if (eat('+')) return parseFactor(); // unary plus
                    if (eat('-')) return -parseFactor(); // unary minus

                    double x;
                    int startPos = this.pos;
                    if (eat('(')) { // parentheses
                        x = parseExpression();
                        eat(')');
                    } else if (Character.isDigit(ch) || ch == '.') { // numbers
                        while (Character.isDigit(ch) || ch == '.') nextChar();
                        x = Double.parseDouble(expression.substring(startPos, this.pos));
                    } else {
                        throw new RuntimeException("Unexpected: " + (char) ch);
                    }

                    return x;
                }
            }.parse();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid expression: " + expression);
        }
}
private boolean isValidHexadecimal(String hex) {
    // Check if the string consists of valid hexadecimal characters
    return hex.matches("^[0-9A-Fa-f]+$");
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
  
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        InputOutput = new javax.swing.JTextField();
        Sign = new javax.swing.JButton();
        A = new javax.swing.JButton();
        F = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        B = new javax.swing.JButton();
        Seven = new javax.swing.JButton();
        Eight = new javax.swing.JButton();
        Nine = new javax.swing.JButton();
        Minus = new javax.swing.JButton();
        C = new javax.swing.JButton();
        Four = new javax.swing.JButton();
        Five = new javax.swing.JButton();
        Six = new javax.swing.JButton();
        Divide = new javax.swing.JButton();
        D = new javax.swing.JButton();
        One = new javax.swing.JButton();
        Two = new javax.swing.JButton();
        Three = new javax.swing.JButton();
        Times = new javax.swing.JButton();
        E = new javax.swing.JButton();
        Zero = new javax.swing.JButton();
        Dot = new javax.swing.JButton();
        Equals = new javax.swing.JButton();
        Plus = new javax.swing.JButton();
        c = new javax.swing.JCheckBox();
        h = new javax.swing.JCheckBox();
        Equation = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(460, 570));
        jPanel1.setLayout(null);

        InputOutput.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jPanel1.add(InputOutput);
        InputOutput.setBounds(30, 40, 390, 90);

        Sign.setBackground(new java.awt.Color(204, 204, 204));
        Sign.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Sign.setText("+/-");
        Sign.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Sign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignActionPerformed(evt);
            }
        });
        jPanel1.add(Sign);
        Sign.setBounds(350, 170, 70, 60);

        A.setBackground(new java.awt.Color(204, 204, 204));
        A.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        A.setText("A");
        A.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        A.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AActionPerformed(evt);
            }
        });
        jPanel1.add(A);
        A.setBounds(30, 170, 70, 60);

        F.setBackground(new java.awt.Color(204, 204, 204));
        F.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        F.setText("F");
        F.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        F.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FActionPerformed(evt);
            }
        });
        jPanel1.add(F);
        F.setBounds(110, 170, 70, 60);

        jButton4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton4.setText("DEL");
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(190, 170, 70, 60);

        jButton5.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jButton5.setText("AC");
        jButton5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);
        jButton5.setBounds(270, 170, 70, 60);

        B.setBackground(new java.awt.Color(204, 204, 204));
        B.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        B.setText("B");
        B.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BActionPerformed(evt);
            }
        });
        jPanel1.add(B);
        B.setBounds(30, 240, 70, 60);

        Seven.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Seven.setText("7");
        Seven.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Seven.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SevenActionPerformed(evt);
            }
        });
        jPanel1.add(Seven);
        Seven.setBounds(110, 240, 70, 60);

        Eight.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Eight.setText("8");
        Eight.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Eight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EightActionPerformed(evt);
            }
        });
        jPanel1.add(Eight);
        Eight.setBounds(190, 240, 70, 60);

        Nine.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Nine.setText("9");
        Nine.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Nine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NineActionPerformed(evt);
            }
        });
        jPanel1.add(Nine);
        Nine.setBounds(270, 240, 70, 60);

        Minus.setBackground(new java.awt.Color(204, 204, 204));
        Minus.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Minus.setText("-");
        Minus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Minus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MinusActionPerformed(evt);
            }
        });
        jPanel1.add(Minus);
        Minus.setBounds(350, 240, 70, 60);

        C.setBackground(new java.awt.Color(204, 204, 204));
        C.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        C.setText("C");
        C.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        C.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CActionPerformed(evt);
            }
        });
        jPanel1.add(C);
        C.setBounds(30, 310, 70, 60);

        Four.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Four.setText("4");
        Four.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Four.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FourActionPerformed(evt);
            }
        });
        jPanel1.add(Four);
        Four.setBounds(110, 310, 70, 60);

        Five.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Five.setText("5");
        Five.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Five.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiveActionPerformed(evt);
            }
        });
        jPanel1.add(Five);
        Five.setBounds(190, 310, 70, 60);

        Six.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Six.setText("6");
        Six.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Six.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SixActionPerformed(evt);
            }
        });
        jPanel1.add(Six);
        Six.setBounds(270, 310, 70, 60);

        Divide.setBackground(new java.awt.Color(204, 204, 204));
        Divide.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Divide.setText("÷");
        Divide.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Divide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DivideActionPerformed(evt);
            }
        });
        jPanel1.add(Divide);
        Divide.setBounds(350, 310, 70, 60);

        D.setBackground(new java.awt.Color(204, 204, 204));
        D.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        D.setText("D");
        D.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DActionPerformed(evt);
            }
        });
        jPanel1.add(D);
        D.setBounds(30, 380, 70, 60);

        One.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        One.setText("1");
        One.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        One.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OneActionPerformed(evt);
            }
        });
        jPanel1.add(One);
        One.setBounds(110, 380, 70, 60);

        Two.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Two.setText("2");
        Two.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Two.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TwoActionPerformed(evt);
            }
        });
        jPanel1.add(Two);
        Two.setBounds(190, 380, 70, 60);

        Three.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Three.setText("3");
        Three.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Three.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ThreeActionPerformed(evt);
            }
        });
        jPanel1.add(Three);
        Three.setBounds(270, 380, 70, 60);

        Times.setBackground(new java.awt.Color(204, 204, 204));
        Times.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Times.setText("x");
        Times.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Times.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TimesActionPerformed(evt);
            }
        });
        jPanel1.add(Times);
        Times.setBounds(350, 380, 70, 60);

        E.setBackground(new java.awt.Color(204, 204, 204));
        E.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        E.setText("E");
        E.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        E.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EActionPerformed(evt);
            }
        });
        jPanel1.add(E);
        E.setBounds(30, 450, 70, 60);

        Zero.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Zero.setText("0");
        Zero.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Zero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZeroActionPerformed(evt);
            }
        });
        jPanel1.add(Zero);
        Zero.setBounds(110, 450, 70, 60);

        Dot.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Dot.setText(".");
        Dot.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Dot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DotActionPerformed(evt);
            }
        });
        jPanel1.add(Dot);
        Dot.setBounds(190, 450, 70, 60);

        Equals.setBackground(new java.awt.Color(204, 204, 204));
        Equals.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Equals.setText("=");
        Equals.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Equals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EqualsActionPerformed(evt);
            }
        });
        jPanel1.add(Equals);
        Equals.setBounds(270, 450, 70, 60);

        Plus.setBackground(new java.awt.Color(204, 204, 204));
        Plus.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        Plus.setText("+");
        Plus.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Plus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlusActionPerformed(evt);
            }
        });
        jPanel1.add(Plus);
        Plus.setBounds(350, 450, 70, 60);

        c.setText("Cal");
        c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cActionPerformed(evt);
            }
        });
        jPanel1.add(c);
        c.setBounds(300, 140, 60, 20);

        h.setText("Hex");
        h.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hActionPerformed(evt);
            }
        });
        jPanel1.add(h);
        h.setBounds(370, 140, 50, 20);
        jPanel1.add(Equation);
        Equation.setBounds(290, 10, 0, 0);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void hActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hActionPerformed
    c.setSelected(false);
    A.setEnabled(true);
    B.setEnabled(true);
    C.setEnabled(true);
    D.setEnabled(true);
    E.setEnabled(true);
    F.setEnabled(true);
    Dot.setEnabled(true);
    Sign.setEnabled(false);

    // Enable only the Minus operator, disable others
    Times.setEnabled(false);
    Divide.setEnabled(false);
    Plus.setEnabled(false);

    InputOutput.setText("");


    }//GEN-LAST:event_hActionPerformed

    private void AActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AActionPerformed
        InputOutput.setText(InputOutput.getText()+"A");
       Equation.setText(Equation.getText()+"A");
    }//GEN-LAST:event_AActionPerformed

    private void BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BActionPerformed
        InputOutput.setText(InputOutput.getText()+"B");
       Equation.setText(Equation.getText()+"B");
    }//GEN-LAST:event_BActionPerformed

    private void CActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CActionPerformed
        InputOutput.setText(InputOutput.getText()+"C");
       Equation.setText(Equation.getText()+"C");
    }//GEN-LAST:event_CActionPerformed

    private void DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DActionPerformed
        InputOutput.setText(InputOutput.getText()+"D");
       Equation.setText(Equation.getText()+"D");
    }//GEN-LAST:event_DActionPerformed

    private void EActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EActionPerformed
        InputOutput.setText(InputOutput.getText()+"E");
       Equation.setText(Equation.getText()+"E");
    }//GEN-LAST:event_EActionPerformed

    private void FActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FActionPerformed
        InputOutput.setText(InputOutput.getText()+"F");
       Equation.setText(Equation.getText()+"F");
    }//GEN-LAST:event_FActionPerformed

    private void ZeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZeroActionPerformed
        InputOutput.setText(InputOutput.getText()+"0");
         Equation.setText(Equation.getText()+"0");
          operate = false;
    }//GEN-LAST:event_ZeroActionPerformed

    private void OneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OneActionPerformed
        InputOutput.setText(InputOutput.getText()+"1");
         Equation.setText(Equation.getText()+"1");
          operate = false;
    }//GEN-LAST:event_OneActionPerformed

    private void TwoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TwoActionPerformed
        InputOutput.setText(InputOutput.getText()+"2");
         Equation.setText(Equation.getText()+"2");
          operate = false;
    }//GEN-LAST:event_TwoActionPerformed

    private void ThreeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ThreeActionPerformed
        InputOutput.setText(InputOutput.getText()+"3");
         Equation.setText(Equation.getText()+"3");
          operate = false;
    }//GEN-LAST:event_ThreeActionPerformed

    private void FourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FourActionPerformed
        InputOutput.setText(InputOutput.getText()+"4");
         Equation.setText(Equation.getText()+"4");
          operate = false;
    }//GEN-LAST:event_FourActionPerformed

    private void FiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FiveActionPerformed
        InputOutput.setText(InputOutput.getText()+"5");
         Equation.setText(Equation.getText()+"5");
          operate = false;
    }//GEN-LAST:event_FiveActionPerformed

    private void SixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SixActionPerformed
        InputOutput.setText(InputOutput.getText()+"6");
         Equation.setText(Equation.getText()+"6");
          operate = false;
    }//GEN-LAST:event_SixActionPerformed

    private void SevenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SevenActionPerformed
        InputOutput.setText(InputOutput.getText()+"7");
         Equation.setText(Equation.getText()+"7");
          operate = false;
    }//GEN-LAST:event_SevenActionPerformed

    private void EightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EightActionPerformed
        InputOutput.setText(InputOutput.getText()+"8");
         Equation.setText(Equation.getText()+"8");
          operate = false;
    }//GEN-LAST:event_EightActionPerformed

    private void NineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NineActionPerformed
        InputOutput.setText(InputOutput.getText()+"9");
         Equation.setText(Equation.getText()+"9");
          operate = false;
    }//GEN-LAST:event_NineActionPerformed

    private void EqualsActionPerformed(java.awt.event.ActionEvent evt) {                                       
        if(c.isSelected()){
            calculate();
        } else if (h.isSelected()){
            hexMinus();
        } else {
            // Additional logic for other modes (if applicable)
        } 
    }
    

    private void PlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlusActionPerformed
        if (!operate) {
            InputOutput.setText(InputOutput.getText() + "+");
            operate = true;
        }
       decimalAdded = false;   
    }//GEN-LAST:event_PlusActionPerformed

    private void TimesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TimesActionPerformed
        if (!operate) {
            InputOutput.setText(InputOutput.getText() + "*");
            operate = true;
        }
       decimalAdded = false;  
    }//GEN-LAST:event_TimesActionPerformed

    private void DivideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DivideActionPerformed
            if (!operate) {
        if(InputOutput.getText().equals("")){
           operate = false; 
        }else{
        InputOutput.setText(InputOutput.getText() + "÷");
        operate = true;
        }
    }
        decimalAdded = false;
         
    }//GEN-LAST:event_DivideActionPerformed

    private void MinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MinusActionPerformed
        if (!operate) {
        if(InputOutput.getText().equals("")){
           operate = false; 
        }else{
        InputOutput.setText(InputOutput.getText() + "-");
        operate = true;
        }
    }
        decimalAdded = false;
    }//GEN-LAST:event_MinusActionPerformed

    private void SignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignActionPerformed
         try {
    String input = InputOutput.getText();
        int lastIndex = -1;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '÷') {
                lastIndex = i;
            }
        }
        if (lastIndex != -1) {
            String beforeOperator = input.substring(0, lastIndex + 1);
            String afterOperator = input.substring(lastIndex + 1);
            if (afterOperator.contains(".")) {
                double number = Double.parseDouble(afterOperator);
                number = -number;
                afterOperator = String.valueOf(number);
            } else {
                int number = Integer.parseInt(afterOperator);
                number = -number;
                afterOperator = String.valueOf(number);
            }
            String updatedInput = beforeOperator + afterOperator;
            InputOutput.setText(updatedInput.replaceAll("--", ""));
        } else { 
            if (input.startsWith("-")) { 
                InputOutput.setText(input.substring(1));
            } else { 
                InputOutput.setText("-" + input);
            }
        }
    } catch (NumberFormatException ex) { // Handle NumberFormatException
    }
    }                                    

    private void ClearAllActionPerformed(java.awt.event.ActionEvent evt) {                                         
        InputOutput.setText("");
              decimalAdded = false;
        operate = false;
    }//GEN-LAST:event_SignActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        InputOutput.setText("");
              decimalAdded = false;
        operate = false;
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
         int length = InputOutput.getText().length();
       int num = InputOutput.getText().length()-1;
       
       String store;
       
       if (length > 0){
       StringBuilder back = new StringBuilder (InputOutput.getText());
       back.deleteCharAt(num);
       store = back.toString();
       InputOutput.setText(store);
       }
         decimalAdded = false;
         operate = false;
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cActionPerformed
         h.setSelected(false);
        A.setEnabled(false);
        B.setEnabled(false);
        C.setEnabled(false);
        D.setEnabled(false);
        E.setEnabled(false);
        F.setEnabled(false);
        Sign.setEnabled(true);
        Times.setEnabled(true);
        Divide.setEnabled(true);
        Minus.setEnabled(true);
        Dot.setEnabled(true);
        Plus.setEnabled(true);
        InputOutput.setText("");
    }//GEN-LAST:event_cActionPerformed

    private void DotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DotActionPerformed
        if (!decimalAdded) {
            InputOutput.setText(InputOutput.getText() + ".");
            decimalAdded = true;
        }
    }//GEN-LAST:event_DotActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CalculatorMidterm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CalculatorMidterm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CalculatorMidterm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CalculatorMidterm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CalculatorMidterm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton A;
    private javax.swing.JButton B;
    private javax.swing.JButton C;
    private javax.swing.JButton D;
    private javax.swing.JButton Divide;
    private javax.swing.JButton Dot;
    private javax.swing.JButton E;
    private javax.swing.JButton Eight;
    private javax.swing.JButton Equals;
    private javax.swing.JLabel Equation;
    private javax.swing.JButton F;
    private javax.swing.JButton Five;
    private javax.swing.JButton Four;
    private javax.swing.JTextField InputOutput;
    private javax.swing.JButton Minus;
    private javax.swing.JButton Nine;
    private javax.swing.JButton One;
    private javax.swing.JButton Plus;
    private javax.swing.JButton Seven;
    private javax.swing.JButton Sign;
    private javax.swing.JButton Six;
    private javax.swing.JButton Three;
    private javax.swing.JButton Times;
    private javax.swing.JButton Two;
    private javax.swing.JButton Zero;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox c;
    private javax.swing.JCheckBox h;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
