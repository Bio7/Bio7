/*
 * $Id$
 *
 * Copyright (c) 2004-2005 by the TeXlapse Team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package net.sourceforge.texlipse.bibparser;

import net.sourceforge.texlipse.bibparser.lexer.Lexer;
import net.sourceforge.texlipse.bibparser.lexer.LexerException;
import net.sourceforge.texlipse.bibparser.node.EOF;
import net.sourceforge.texlipse.bibparser.node.TLBrace;
import net.sourceforge.texlipse.bibparser.node.TRBrace;
import net.sourceforge.texlipse.bibparser.node.TStringLiteral;
import net.sourceforge.texlipse.bibparser.node.TWhitespace;
import net.sourceforge.texlipse.bibparser.node.Token;

/**
 * BibTeX lexer. Extends the lexer generated by SableCC from the
 * generation file. Implements handling strings with matched braces.
 * 
 * @author Oskar Ojala
 */
public class BibLexer extends Lexer {

    private int count;
    
    private Token stringStart;
    private StringBuffer text;
    
    /**
     * Constructs a new lexer.
     * 
     * @param in The reader to read the characters from
     */
    public BibLexer(java.io.PushbackReader in) {
        super(in);
    }

    /**
     * We define a filter that recognizes braced strings and removes
     * the preamble and scribe-comments
     */
    protected void filter() throws LexerException {
        
        if (state.equals(State.BRACESTRING)) {
            
            // if we are just entering this state, first token is '{'
            if (stringStart == null) {
                
                stringStart = token;
                text = new StringBuffer("");
                count = 1;
                token = null; // continue to scan the input.
                
            } else {
                if (token instanceof TLBrace)
                    count++;
                else if (token instanceof TRBrace)
                    count--;
                else if (token instanceof EOF) {
                    throw new LexerException("[" + stringStart.getLine() + 
                            "," + stringStart.getPos() + "] Unexpected end of file");
                }
                if (count != 0) {
                    // accumulate the string and continue to scan the input.
                    if (token instanceof TWhitespace)
                        text.append(" ");
                    else
                        text.append(token.getText());
                    token = null;
                } else {
                    TStringLiteral tsl = new TStringLiteral(text.toString(),
                            stringStart.getLine(),
                            stringStart.getPos());
                    // emit the string
                    token = tsl;
                    state = State.ASSIGN; //go back to assign mode
                    stringStart = null;
                }
            }
        } else if (state.equals(State.REMOVE)) {
            if (token instanceof TLBrace) {
                count++;
                if (stringStart == null)
                    stringStart = token;
            } else if (token instanceof TRBrace) {
                count--;
            } else if (token instanceof EOF) {
                throw new LexerException("[" + stringStart.getLine() + 
                        "," + stringStart.getPos() + "] Unexpected end of file");
            }
            token = null;
            if (count == 0 && stringStart != null) {
                state = State.NORMAL;
                stringStart = null;
            }
        }
    }
}
