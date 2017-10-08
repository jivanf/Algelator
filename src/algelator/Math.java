package algelator;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.*;
        
import javafx.scene.control.*;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jivan108
 */
public class Math {
        
    public static void main(String[] args) {
    
        Scanner scanner = new Scanner( System.in );

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        System.out.println("Would you like to use function notation?");

        String ans = scanner.nextLine();
        
        System.out.println( "Expression: " );

        String exp = scanner.nextLine();

        ArrayList<Integer> realValues = new ArrayList<Integer>();

        final Pattern varPattern = Pattern.compile("[a-zA-Z]");
        final Pattern funcPattern = Pattern.compile("\\w+\\([a-zA-Z]\\)=[A-Za-z0-9-!$%^&*()+ {}|/~=`:'<>?,.]+");

        Matcher varMatcher = varPattern.matcher(exp);

        Map<String, Integer> vars = new HashMap<String, Integer>();

        String funcNotationInput = null;
        String funcNotation[] = null;


        // Check if user selected function notation
        if (ans.equals("y") || ans.equals("Y")) {
            System.out.println("Function notation: ");
            funcNotationInput = scanner.nextLine().replaceAll("\\s+", "").trim();
            funcNotation = funcNotationInput.split("=");
            int openBraceIndex = funcNotation[0].indexOf("(");
            int closeBraceIndex = funcNotation[0].indexOf(")");

            // Check if we are getting multiple values for variable
            if (!funcNotationInput.isEmpty()) {
                if (funcNotation[1].charAt(0) == '{') {
                    String plainValues[] = funcNotation[1].split(",");

                    for (String i : plainValues) {
                        i = i.replaceAll("\\{", "");
                        i = i.replaceAll("}", "");
                        realValues.add(Integer.parseInt(i));
                    }
                }
            }

            // Loops until it doesn't find variables in expression
            while (varMatcher.find()) {
                String group = varMatcher.group();

                // Check if variable is not already in function notation
                if (!group.equals(funcNotation[0].substring(openBraceIndex + 1, closeBraceIndex))) {
                    System.out.println("Value of " + group + " is: ");
                    String value = scanner.nextLine();
                    try {
                        vars.put(group, new BigDecimal(engine.eval(value).toString()).intValue());
                    } catch (ScriptException e) {
                        System.out.println("Wrong value");
                    }
                }
            }

            Matcher funcMatcher = funcPattern.matcher(funcNotationInput);

            // Checks if function notation is written correctly
            if (funcMatcher.matches()) {
                try {
                    // Put variable in function notation in vars array
                    vars.put(funcNotation[0].substring(openBraceIndex + 1, closeBraceIndex), new BigDecimal(engine.eval(funcNotation[1]).toString()).intValue());
                } catch (ScriptException e) {
                    System.out.println("SYNTAX error");
                }
            } else {
                System.out.println("SYNTAX error");
            }
        }

        else {
            while (varMatcher.find()) {
                String group = varMatcher.group();

                    System.out.println("Value of " + group + " is: ");
                    String value = scanner.nextLine();
                    try {
                        vars.put(group, new BigDecimal(engine.eval(value).toString()).intValue());
                    } catch (ScriptException e) {
                        System.out.println("Wrong value");
                    }
            }

        }

        // Checks if there are multiple variables in expression
        if (!vars.isEmpty()) {

            // Builds the expression with the variables
            Expression e = new ExpressionBuilder(exp)
                    .variables(vars.keySet())
                    .build();

            Collection keys = vars.keySet();

            // Loops through vars array and sets the value of variable in expression
            for (Object i : keys) {
                if (funcNotationInput != null) {
                    int openBraceIndex = funcNotation[0].indexOf("(");
                    int closeBraceIndex = funcNotation[0].indexOf(")");
                    if (i.equals(funcNotation[0].substring(openBraceIndex + 1, closeBraceIndex))) {
                        if (funcNotation[1].charAt(0) == '{') {
                            for (int x : realValues) {
                                e.setVariable((String) i, x);
                                double result = e.evaluate();
                                System.out.println((int) result);
                            }
                            return;
                        }
                    }
                }
                e.setVariable((String) i, vars.get(i));
            }

            double result = e.evaluate();
            System.out.println((int) result);
        }

        else {
            Expression e = new ExpressionBuilder(exp)
                    .build();
            double result = e.evaluate();
            System.out.println((int) result);
        }
        
        
}
   
}
