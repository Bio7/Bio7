// Generated from R.g4 by ANTLR 4.5.1
package com.eco.bio7.reditor.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class RParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, T__42=43, T__43=44, T__44=45, 
		T__45=46, T__46=47, T__47=48, T__48=49, T__49=50, T__50=51, T__51=52, 
		T__52=53, T__53=54, T__54=55, T__55=56, HEX=57, INT=58, FLOAT=59, COMPLEX=60, 
		STRING=61, ID=62, USER_OP=63, NL=64, WS=65, UNKNOWN=66;
	public static final int
		RULE_prog = 0, RULE_expr = 1, RULE_unknowns = 2, RULE_exprlist = 3, RULE_formlist = 4, 
		RULE_form = 5, RULE_sublist = 6, RULE_sub = 7;
	public static final String[] ruleNames = {
		"prog", "expr", "unknowns", "exprlist", "formlist", "form", "sublist", 
		"sub"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "';'", "'[['", "']'", "'['", "'::'", "':::'", "'$'", "'@'", "'^'", 
		"'-'", "'+'", "':'", "'*'", "'/'", "'>'", "'>='", "'<'", "'<='", "'=='", 
		"'!='", "'!'", "'&'", "'&&'", "'|'", "'||'", "'~'", "'<-'", "'<<-'", "'='", 
		"'->'", "'->>'", "':='", "'{'", "'}'", "'function'", "'('", "')'", "'if'", 
		"'else'", "'for'", "'in'", "'while'", "'repeat'", "'?'", "'next'", "'break'", 
		"'NULL'", "'NA'", "'Inf'", "'NaN'", "'TRUE'", "'FALSE'", "'=>'", "'=<'", 
		"','", "'...'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, "HEX", "INT", "FLOAT", 
		"COMPLEX", "STRING", "ID", "USER_OP", "NL", "WS", "UNKNOWN"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "R.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ProgContext extends ParserRuleContext {
		public List<TerminalNode> EOF() { return getTokens(RParser.EOF); }
		public TerminalNode EOF(int i) {
			return getToken(RParser.EOF, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> NL() { return getTokens(RParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RParser.NL, i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & ((1L << (T__9 - 10)) | (1L << (T__10 - 10)) | (1L << (T__20 - 10)) | (1L << (T__25 - 10)) | (1L << (T__32 - 10)) | (1L << (T__34 - 10)) | (1L << (T__35 - 10)) | (1L << (T__37 - 10)) | (1L << (T__39 - 10)) | (1L << (T__41 - 10)) | (1L << (T__42 - 10)) | (1L << (T__43 - 10)) | (1L << (T__44 - 10)) | (1L << (T__45 - 10)) | (1L << (T__46 - 10)) | (1L << (T__47 - 10)) | (1L << (T__48 - 10)) | (1L << (T__49 - 10)) | (1L << (T__50 - 10)) | (1L << (T__51 - 10)) | (1L << (HEX - 10)) | (1L << (INT - 10)) | (1L << (FLOAT - 10)) | (1L << (COMPLEX - 10)) | (1L << (STRING - 10)) | (1L << (ID - 10)) | (1L << (NL - 10)) | (1L << (UNKNOWN - 10)))) != 0)) {
				{
				setState(20);
				switch (_input.LA(1)) {
				case T__9:
				case T__10:
				case T__20:
				case T__25:
				case T__32:
				case T__34:
				case T__35:
				case T__37:
				case T__39:
				case T__41:
				case T__42:
				case T__43:
				case T__44:
				case T__45:
				case T__46:
				case T__47:
				case T__48:
				case T__49:
				case T__50:
				case T__51:
				case HEX:
				case INT:
				case FLOAT:
				case COMPLEX:
				case STRING:
				case ID:
				case UNKNOWN:
					{
					setState(16);
					expr(0);
					setState(17);
					_la = _input.LA(1);
					if ( !(_la==EOF || _la==T__0 || _la==NL) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					break;
				case NL:
					{
					setState(19);
					match(NL);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(24);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(25);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Err11Context extends ExprContext {
		public Token extra;
		public ExprlistContext exprlist() {
			return getRuleContext(ExprlistContext.class,0);
		}
		public Err11Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr11(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr11(this);
		}
	}
	public static class E31Context extends ExprContext {
		public TerminalNode STRING() { return getToken(RParser.STRING, 0); }
		public E31Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE31(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE31(this);
		}
	}
	public static class E30Context extends ExprContext {
		public TerminalNode ID() { return getToken(RParser.ID, 0); }
		public E30Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE30(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE30(this);
		}
	}
	public static class E32Context extends ExprContext {
		public TerminalNode HEX() { return getToken(RParser.HEX, 0); }
		public TerminalNode INT() { return getToken(RParser.INT, 0); }
		public E32Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE32(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE32(this);
		}
	}
	public static class E35Context extends ExprContext {
		public TerminalNode COMPLEX() { return getToken(RParser.COMPLEX, 0); }
		public E35Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE35(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE35(this);
		}
	}
	public static class E34Context extends ExprContext {
		public TerminalNode FLOAT() { return getToken(RParser.FLOAT, 0); }
		public E34Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE34(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE34(this);
		}
	}
	public static class E37Context extends ExprContext {
		public E37Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE37(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE37(this);
		}
	}
	public static class Err18Context extends ExprContext {
		public Token extra;
		public TerminalNode ID() { return getToken(RParser.ID, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Err18Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr18(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr18(this);
		}
	}
	public static class E36Context extends ExprContext {
		public E36Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE36(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE36(this);
		}
	}
	public static class E39Context extends ExprContext {
		public E39Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE39(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE39(this);
		}
	}
	public static class E38Context extends ExprContext {
		public E38Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE38(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE38(this);
		}
	}
	public static class Err16Context extends ExprContext {
		public Token extra;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Err16Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr16(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr16(this);
		}
	}
	public static class E40Context extends ExprContext {
		public E40Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE40(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE40(this);
		}
	}
	public static class E17VariableDeclarationContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E17VariableDeclarationContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE17VariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE17VariableDeclaration(this);
		}
	}
	public static class Err21Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Err21Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr21(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr21(this);
		}
	}
	public static class Err22Context extends ExprContext {
		public UnknownsContext unknowns() {
			return getRuleContext(UnknownsContext.class,0);
		}
		public Err22Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr22(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr22(this);
		}
	}
	public static class E41Context extends ExprContext {
		public E41Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE41(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE41(this);
		}
	}
	public static class Err20Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Err20Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr20(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr20(this);
		}
	}
	public static class E19DefFunctionContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FormlistContext formlist() {
			return getRuleContext(FormlistContext.class,0);
		}
		public E19DefFunctionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE19DefFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE19DefFunction(this);
		}
	}
	public static class Err3Context extends ExprContext {
		public Token extra;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SublistContext sublist() {
			return getRuleContext(SublistContext.class,0);
		}
		public Err3Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr3(this);
		}
	}
	public static class Err1Context extends ExprContext {
		public Token extra;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Err1Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr1(this);
		}
	}
	public static class E11Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E11Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE11(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE11(this);
		}
	}
	public static class E20CallFunctionContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SublistContext sublist() {
			return getRuleContext(SublistContext.class,0);
		}
		public E20CallFunctionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE20CallFunction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE20CallFunction(this);
		}
	}
	public static class E10Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E10Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE10(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE10(this);
		}
	}
	public static class E13Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E13Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE13(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE13(this);
		}
	}
	public static class E12Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public E12Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE12(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE12(this);
		}
	}
	public static class E15Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public E15Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE15(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE15(this);
		}
	}
	public static class E14Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E14Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE14(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE14(this);
		}
	}
	public static class E16Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E16Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE16(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE16(this);
		}
	}
	public static class E18Context extends ExprContext {
		public ExprlistContext exprlist() {
			return getRuleContext(ExprlistContext.class,0);
		}
		public E18Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE18(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE18(this);
		}
	}
	public static class Err9Context extends ExprContext {
		public Token extra;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SublistContext sublist() {
			return getRuleContext(SublistContext.class,0);
		}
		public Err9Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr9(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr9(this);
		}
	}
	public static class Err8Context extends ExprContext {
		public Token extra;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SublistContext sublist() {
			return getRuleContext(SublistContext.class,0);
		}
		public Err8Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr8(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr8(this);
		}
	}
	public static class Err7Context extends ExprContext {
		public Token extra;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Err7Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr7(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr7(this);
		}
	}
	public static class Err5Context extends ExprContext {
		public Token extra;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FormlistContext formlist() {
			return getRuleContext(FormlistContext.class,0);
		}
		public Err5Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterErr5(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitErr5(this);
		}
	}
	public static class E22Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E22Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE22(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE22(this);
		}
	}
	public static class E21Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E21Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE21(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE21(this);
		}
	}
	public static class E24Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E24Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE24(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE24(this);
		}
	}
	public static class E23Context extends ExprContext {
		public TerminalNode ID() { return getToken(RParser.ID, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E23Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE23(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE23(this);
		}
	}
	public static class E26Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public E26Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE26(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE26(this);
		}
	}
	public static class E25Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public E25Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE25(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE25(this);
		}
	}
	public static class E28Context extends ExprContext {
		public E28Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE28(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE28(this);
		}
	}
	public static class E27Context extends ExprContext {
		public E27Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE27(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE27(this);
		}
	}
	public static class E1Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SublistContext sublist() {
			return getRuleContext(SublistContext.class,0);
		}
		public E1Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE1(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE1(this);
		}
	}
	public static class E29Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public E29Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE29(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE29(this);
		}
	}
	public static class E2Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SublistContext sublist() {
			return getRuleContext(SublistContext.class,0);
		}
		public E2Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE2(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE2(this);
		}
	}
	public static class E3Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E3Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE3(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE3(this);
		}
	}
	public static class E4Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E4Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE4(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE4(this);
		}
	}
	public static class E5Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E5Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE5(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE5(this);
		}
	}
	public static class E6Context extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public E6Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE6(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE6(this);
		}
	}
	public static class E7Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E7Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE7(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE7(this);
		}
	}
	public static class E8Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode USER_OP() { return getToken(RParser.USER_OP, 0); }
		public E8Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE8(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE8(this);
		}
	}
	public static class E9Context extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public E9Context(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterE9(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitE9(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				_localctx = new E6Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(28);
				_la = _input.LA(1);
				if ( !(_la==T__9 || _la==T__10) ) {
				_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(29);
				expr(48);
				}
				break;
			case 2:
				{
				_localctx = new E12Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(30);
				match(T__20);
				setState(31);
				expr(42);
				}
				break;
			case 3:
				{
				_localctx = new E15Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(32);
				match(T__25);
				setState(33);
				expr(39);
				}
				break;
			case 4:
				{
				_localctx = new E19DefFunctionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(34);
				match(T__34);
				setState(35);
				match(T__35);
				setState(37);
				_la = _input.LA(1);
				if (_la==T__55 || _la==ID) {
					{
					setState(36);
					formlist();
					}
				}

				setState(39);
				match(T__36);
				setState(40);
				expr(35);
				}
				break;
			case 5:
				{
				_localctx = new E25Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(41);
				match(T__42);
				setState(42);
				expr(29);
				}
				break;
			case 6:
				{
				_localctx = new E26Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(43);
				match(T__43);
				setState(44);
				expr(28);
				}
				break;
			case 7:
				{
				_localctx = new Err5Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(45);
				match(T__34);
				setState(46);
				match(T__35);
				setState(48);
				_la = _input.LA(1);
				if (_la==T__55 || _la==ID) {
					{
					setState(47);
					formlist();
					}
				}

				setState(50);
				match(T__36);
				setState(51);
				((Err5Context)_localctx).extra = match(T__36);
				setState(52);
				expr(10);
				}
				break;
			case 8:
				{
				_localctx = new E18Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(53);
				match(T__32);
				setState(54);
				exprlist();
				setState(55);
				match(T__33);
				}
				break;
			case 9:
				{
				_localctx = new E21Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(57);
				match(T__37);
				setState(58);
				match(T__35);
				setState(59);
				expr(0);
				setState(60);
				match(T__36);
				setState(61);
				expr(0);
				}
				break;
			case 10:
				{
				_localctx = new E22Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(63);
				match(T__37);
				setState(64);
				match(T__35);
				setState(65);
				expr(0);
				setState(66);
				match(T__36);
				setState(67);
				expr(0);
				setState(68);
				match(T__38);
				setState(69);
				expr(0);
				}
				break;
			case 11:
				{
				_localctx = new E23Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(71);
				match(T__39);
				setState(72);
				match(T__35);
				setState(73);
				match(ID);
				setState(74);
				match(T__40);
				setState(75);
				expr(0);
				setState(76);
				match(T__36);
				setState(77);
				expr(0);
				}
				break;
			case 12:
				{
				_localctx = new E24Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(79);
				match(T__41);
				setState(80);
				match(T__35);
				setState(81);
				expr(0);
				setState(82);
				match(T__36);
				setState(83);
				expr(0);
				}
				break;
			case 13:
				{
				_localctx = new E27Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(85);
				match(T__44);
				}
				break;
			case 14:
				{
				_localctx = new E28Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(86);
				match(T__45);
				}
				break;
			case 15:
				{
				_localctx = new E29Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(87);
				match(T__35);
				setState(88);
				expr(0);
				setState(89);
				match(T__36);
				}
				break;
			case 16:
				{
				_localctx = new E30Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(91);
				match(ID);
				}
				break;
			case 17:
				{
				_localctx = new E31Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(92);
				match(STRING);
				}
				break;
			case 18:
				{
				_localctx = new E32Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(93);
				match(HEX);
				}
				break;
			case 19:
				{
				_localctx = new E32Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(94);
				match(INT);
				}
				break;
			case 20:
				{
				_localctx = new E34Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(95);
				match(FLOAT);
				}
				break;
			case 21:
				{
				_localctx = new E35Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(96);
				match(COMPLEX);
				}
				break;
			case 22:
				{
				_localctx = new E36Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(97);
				match(T__46);
				}
				break;
			case 23:
				{
				_localctx = new E37Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(98);
				match(T__47);
				}
				break;
			case 24:
				{
				_localctx = new E38Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(99);
				match(T__48);
				}
				break;
			case 25:
				{
				_localctx = new E39Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(100);
				match(T__49);
				}
				break;
			case 26:
				{
				_localctx = new E40Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(101);
				match(T__50);
				}
				break;
			case 27:
				{
				_localctx = new E41Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(102);
				match(T__51);
				}
				break;
			case 28:
				{
				_localctx = new Err1Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(103);
				match(T__35);
				setState(104);
				expr(0);
				setState(105);
				match(T__36);
				setState(106);
				((Err1Context)_localctx).extra = match(T__36);
				}
				break;
			case 29:
				{
				_localctx = new Err7Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(108);
				match(T__37);
				setState(109);
				match(T__35);
				setState(110);
				expr(0);
				setState(111);
				match(T__36);
				setState(112);
				((Err7Context)_localctx).extra = match(T__36);
				setState(113);
				expr(0);
				}
				break;
			case 30:
				{
				_localctx = new Err11Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(115);
				match(T__32);
				setState(116);
				exprlist();
				setState(117);
				match(T__33);
				setState(118);
				((Err11Context)_localctx).extra = match(T__33);
				}
				break;
			case 31:
				{
				_localctx = new Err16Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(120);
				match(T__41);
				setState(121);
				match(T__35);
				setState(122);
				expr(0);
				setState(123);
				match(T__36);
				setState(124);
				((Err16Context)_localctx).extra = match(T__36);
				setState(125);
				expr(0);
				}
				break;
			case 32:
				{
				_localctx = new Err18Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(127);
				match(T__39);
				setState(128);
				match(T__35);
				setState(129);
				match(ID);
				setState(130);
				match(T__40);
				setState(131);
				expr(0);
				setState(132);
				match(T__36);
				setState(133);
				((Err18Context)_localctx).extra = match(T__36);
				setState(134);
				expr(0);
				}
				break;
			case 33:
				{
				_localctx = new Err22Context(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(136);
				unknowns();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(218);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(216);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new E3Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(139);
						if (!(precpred(_ctx, 51))) throw new FailedPredicateException(this, "precpred(_ctx, 51)");
						setState(140);
						_la = _input.LA(1);
						if ( !(_la==T__4 || _la==T__5) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(141);
						expr(52);
						}
						break;
					case 2:
						{
						_localctx = new E4Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(142);
						if (!(precpred(_ctx, 50))) throw new FailedPredicateException(this, "precpred(_ctx, 50)");
						setState(143);
						_la = _input.LA(1);
						if ( !(_la==T__6 || _la==T__7) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(144);
						expr(51);
						}
						break;
					case 3:
						{
						_localctx = new E5Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(145);
						if (!(precpred(_ctx, 49))) throw new FailedPredicateException(this, "precpred(_ctx, 49)");
						setState(146);
						match(T__8);
						setState(147);
						expr(50);
						}
						break;
					case 4:
						{
						_localctx = new E7Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(148);
						if (!(precpred(_ctx, 47))) throw new FailedPredicateException(this, "precpred(_ctx, 47)");
						setState(149);
						match(T__11);
						setState(150);
						expr(48);
						}
						break;
					case 5:
						{
						_localctx = new E8Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(151);
						if (!(precpred(_ctx, 46))) throw new FailedPredicateException(this, "precpred(_ctx, 46)");
						setState(152);
						match(USER_OP);
						setState(153);
						expr(47);
						}
						break;
					case 6:
						{
						_localctx = new E9Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(154);
						if (!(precpred(_ctx, 45))) throw new FailedPredicateException(this, "precpred(_ctx, 45)");
						setState(155);
						_la = _input.LA(1);
						if ( !(_la==T__12 || _la==T__13) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(156);
						expr(46);
						}
						break;
					case 7:
						{
						_localctx = new E10Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(157);
						if (!(precpred(_ctx, 44))) throw new FailedPredicateException(this, "precpred(_ctx, 44)");
						setState(158);
						_la = _input.LA(1);
						if ( !(_la==T__9 || _la==T__10) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(159);
						expr(45);
						}
						break;
					case 8:
						{
						_localctx = new E11Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(160);
						if (!(precpred(_ctx, 43))) throw new FailedPredicateException(this, "precpred(_ctx, 43)");
						setState(161);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(162);
						expr(44);
						}
						break;
					case 9:
						{
						_localctx = new E13Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(163);
						if (!(precpred(_ctx, 41))) throw new FailedPredicateException(this, "precpred(_ctx, 41)");
						setState(164);
						_la = _input.LA(1);
						if ( !(_la==T__21 || _la==T__22) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(165);
						expr(42);
						}
						break;
					case 10:
						{
						_localctx = new E14Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(166);
						if (!(precpred(_ctx, 40))) throw new FailedPredicateException(this, "precpred(_ctx, 40)");
						setState(167);
						_la = _input.LA(1);
						if ( !(_la==T__23 || _la==T__24) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(168);
						expr(41);
						}
						break;
					case 11:
						{
						_localctx = new E16Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(169);
						if (!(precpred(_ctx, 38))) throw new FailedPredicateException(this, "precpred(_ctx, 38)");
						setState(170);
						match(T__25);
						setState(171);
						expr(39);
						}
						break;
					case 12:
						{
						_localctx = new E17VariableDeclarationContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(172);
						if (!(precpred(_ctx, 37))) throw new FailedPredicateException(this, "precpred(_ctx, 37)");
						setState(173);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << T__30) | (1L << T__31))) != 0)) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(174);
						expr(38);
						}
						break;
					case 13:
						{
						_localctx = new Err20Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(175);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(176);
						match(T__52);
						setState(177);
						expr(4);
						}
						break;
					case 14:
						{
						_localctx = new Err21Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(178);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(179);
						match(T__53);
						setState(180);
						expr(3);
						}
						break;
					case 15:
						{
						_localctx = new E1Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(181);
						if (!(precpred(_ctx, 53))) throw new FailedPredicateException(this, "precpred(_ctx, 53)");
						setState(182);
						match(T__1);
						setState(183);
						sublist();
						setState(184);
						match(T__2);
						setState(185);
						match(T__2);
						}
						break;
					case 16:
						{
						_localctx = new E2Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(187);
						if (!(precpred(_ctx, 52))) throw new FailedPredicateException(this, "precpred(_ctx, 52)");
						setState(188);
						match(T__3);
						setState(189);
						sublist();
						setState(190);
						match(T__2);
						}
						break;
					case 17:
						{
						_localctx = new E20CallFunctionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(192);
						if (!(precpred(_ctx, 34))) throw new FailedPredicateException(this, "precpred(_ctx, 34)");
						setState(193);
						match(T__35);
						setState(194);
						sublist();
						setState(195);
						match(T__36);
						}
						break;
					case 18:
						{
						_localctx = new Err3Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(197);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(198);
						match(T__35);
						setState(199);
						sublist();
						setState(200);
						match(T__36);
						setState(201);
						((Err3Context)_localctx).extra = match(T__36);
						}
						break;
					case 19:
						{
						_localctx = new Err8Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(203);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(204);
						match(T__1);
						setState(205);
						sublist();
						setState(206);
						match(T__2);
						setState(207);
						match(T__2);
						setState(208);
						((Err8Context)_localctx).extra = match(T__2);
						}
						break;
					case 20:
						{
						_localctx = new Err9Context(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(210);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(211);
						match(T__3);
						setState(212);
						sublist();
						setState(213);
						match(T__2);
						setState(214);
						((Err9Context)_localctx).extra = match(T__2);
						}
						break;
					}
					} 
				}
				setState(220);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class UnknownsContext extends ParserRuleContext {
		public List<TerminalNode> UNKNOWN() { return getTokens(RParser.UNKNOWN); }
		public TerminalNode UNKNOWN(int i) {
			return getToken(RParser.UNKNOWN, i);
		}
		public UnknownsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unknowns; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterUnknowns(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitUnknowns(this);
		}
	}

	public final UnknownsContext unknowns() throws RecognitionException {
		UnknownsContext _localctx = new UnknownsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_unknowns);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(222); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(221);
					match(UNKNOWN);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(224); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprlistContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> NL() { return getTokens(RParser.NL); }
		public TerminalNode NL(int i) {
			return getToken(RParser.NL, i);
		}
		public ExprlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterExprlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitExprlist(this);
		}
	}

	public final ExprlistContext exprlist() throws RecognitionException {
		ExprlistContext _localctx = new ExprlistContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_exprlist);
		int _la;
		try {
			setState(237);
			switch (_input.LA(1)) {
			case T__9:
			case T__10:
			case T__20:
			case T__25:
			case T__32:
			case T__34:
			case T__35:
			case T__37:
			case T__39:
			case T__41:
			case T__42:
			case T__43:
			case T__44:
			case T__45:
			case T__46:
			case T__47:
			case T__48:
			case T__49:
			case T__50:
			case T__51:
			case HEX:
			case INT:
			case FLOAT:
			case COMPLEX:
			case STRING:
			case ID:
			case UNKNOWN:
				enterOuterAlt(_localctx, 1);
				{
				setState(226);
				expr(0);
				setState(233);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__0 || _la==NL) {
					{
					{
					setState(227);
					_la = _input.LA(1);
					if ( !(_la==T__0 || _la==NL) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					setState(229);
					_la = _input.LA(1);
					if (((((_la - 10)) & ~0x3f) == 0 && ((1L << (_la - 10)) & ((1L << (T__9 - 10)) | (1L << (T__10 - 10)) | (1L << (T__20 - 10)) | (1L << (T__25 - 10)) | (1L << (T__32 - 10)) | (1L << (T__34 - 10)) | (1L << (T__35 - 10)) | (1L << (T__37 - 10)) | (1L << (T__39 - 10)) | (1L << (T__41 - 10)) | (1L << (T__42 - 10)) | (1L << (T__43 - 10)) | (1L << (T__44 - 10)) | (1L << (T__45 - 10)) | (1L << (T__46 - 10)) | (1L << (T__47 - 10)) | (1L << (T__48 - 10)) | (1L << (T__49 - 10)) | (1L << (T__50 - 10)) | (1L << (T__51 - 10)) | (1L << (HEX - 10)) | (1L << (INT - 10)) | (1L << (FLOAT - 10)) | (1L << (COMPLEX - 10)) | (1L << (STRING - 10)) | (1L << (ID - 10)) | (1L << (UNKNOWN - 10)))) != 0)) {
						{
						setState(228);
						expr(0);
						}
					}

					}
					}
					setState(235);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__33:
				enterOuterAlt(_localctx, 2);
				{
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormlistContext extends ParserRuleContext {
		public List<FormContext> form() {
			return getRuleContexts(FormContext.class);
		}
		public FormContext form(int i) {
			return getRuleContext(FormContext.class,i);
		}
		public FormlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterFormlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitFormlist(this);
		}
	}

	public final FormlistContext formlist() throws RecognitionException {
		FormlistContext _localctx = new FormlistContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_formlist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			form();
			setState(244);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__54) {
				{
				{
				setState(240);
				match(T__54);
				setState(241);
				form();
				}
				}
				setState(246);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(RParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public FormContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_form; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterForm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitForm(this);
		}
	}

	public final FormContext form() throws RecognitionException {
		FormContext _localctx = new FormContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_form);
		try {
			setState(252);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				match(ID);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(248);
				match(ID);
				setState(249);
				match(T__28);
				setState(250);
				expr(0);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(251);
				match(T__55);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SublistContext extends ParserRuleContext {
		public List<SubContext> sub() {
			return getRuleContexts(SubContext.class);
		}
		public SubContext sub(int i) {
			return getRuleContext(SubContext.class,i);
		}
		public SublistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sublist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterSublist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitSublist(this);
		}
	}

	public final SublistContext sublist() throws RecognitionException {
		SublistContext _localctx = new SublistContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_sublist);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			sub();
			setState(259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__54) {
				{
				{
				setState(255);
				match(T__54);
				setState(256);
				sub();
				}
				}
				setState(261);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ID() { return getToken(RParser.ID, 0); }
		public TerminalNode STRING() { return getToken(RParser.STRING, 0); }
		public SubContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sub; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).enterSub(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RListener ) ((RListener)listener).exitSub(this);
		}
	}

	public final SubContext sub() throws RecognitionException {
		SubContext _localctx = new SubContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_sub);
		try {
			setState(280);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(262);
				expr(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(263);
				match(ID);
				setState(264);
				match(T__28);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(265);
				match(ID);
				setState(266);
				match(T__28);
				setState(267);
				expr(0);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(268);
				match(STRING);
				setState(269);
				match(T__28);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(270);
				match(STRING);
				setState(271);
				match(T__28);
				setState(272);
				expr(0);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(273);
				match(T__46);
				setState(274);
				match(T__28);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(275);
				match(T__46);
				setState(276);
				match(T__28);
				setState(277);
				expr(0);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(278);
				match(T__55);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 51);
		case 1:
			return precpred(_ctx, 50);
		case 2:
			return precpred(_ctx, 49);
		case 3:
			return precpred(_ctx, 47);
		case 4:
			return precpred(_ctx, 46);
		case 5:
			return precpred(_ctx, 45);
		case 6:
			return precpred(_ctx, 44);
		case 7:
			return precpred(_ctx, 43);
		case 8:
			return precpred(_ctx, 41);
		case 9:
			return precpred(_ctx, 40);
		case 10:
			return precpred(_ctx, 38);
		case 11:
			return precpred(_ctx, 37);
		case 12:
			return precpred(_ctx, 3);
		case 13:
			return precpred(_ctx, 2);
		case 14:
			return precpred(_ctx, 53);
		case 15:
			return precpred(_ctx, 52);
		case 16:
			return precpred(_ctx, 34);
		case 17:
			return precpred(_ctx, 11);
		case 18:
			return precpred(_ctx, 8);
		case 19:
			return precpred(_ctx, 7);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3D\u011d\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\2"+
		"\7\2\27\n\2\f\2\16\2\32\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\5\3(\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\63\n\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u008c"+
		"\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3\u00db\n\3\f\3\16\3\u00de\13\3\3\4"+
		"\6\4\u00e1\n\4\r\4\16\4\u00e2\3\5\3\5\3\5\5\5\u00e8\n\5\7\5\u00ea\n\5"+
		"\f\5\16\5\u00ed\13\5\3\5\5\5\u00f0\n\5\3\6\3\6\3\6\7\6\u00f5\n\6\f\6\16"+
		"\6\u00f8\13\6\3\7\3\7\3\7\3\7\3\7\5\7\u00ff\n\7\3\b\3\b\3\b\7\b\u0104"+
		"\n\b\f\b\16\b\u0107\13\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\t\5\t\u011b\n\t\3\t\2\3\4\n\2\4\6\b\n\f\16\20"+
		"\2\f\4\3\3\3BB\3\2\f\r\3\2\7\b\3\2\t\n\3\2\17\20\3\2\21\26\3\2\30\31\3"+
		"\2\32\33\3\2\35\"\4\2\3\3BB\u015c\2\30\3\2\2\2\4\u008b\3\2\2\2\6\u00e0"+
		"\3\2\2\2\b\u00ef\3\2\2\2\n\u00f1\3\2\2\2\f\u00fe\3\2\2\2\16\u0100\3\2"+
		"\2\2\20\u011a\3\2\2\2\22\23\5\4\3\2\23\24\t\2\2\2\24\27\3\2\2\2\25\27"+
		"\7B\2\2\26\22\3\2\2\2\26\25\3\2\2\2\27\32\3\2\2\2\30\26\3\2\2\2\30\31"+
		"\3\2\2\2\31\33\3\2\2\2\32\30\3\2\2\2\33\34\7\2\2\3\34\3\3\2\2\2\35\36"+
		"\b\3\1\2\36\37\t\3\2\2\37\u008c\5\4\3\62 !\7\27\2\2!\u008c\5\4\3,\"#\7"+
		"\34\2\2#\u008c\5\4\3)$%\7%\2\2%\'\7&\2\2&(\5\n\6\2\'&\3\2\2\2\'(\3\2\2"+
		"\2()\3\2\2\2)*\7\'\2\2*\u008c\5\4\3%+,\7-\2\2,\u008c\5\4\3\37-.\7.\2\2"+
		".\u008c\5\4\3\36/\60\7%\2\2\60\62\7&\2\2\61\63\5\n\6\2\62\61\3\2\2\2\62"+
		"\63\3\2\2\2\63\64\3\2\2\2\64\65\7\'\2\2\65\66\7\'\2\2\66\u008c\5\4\3\f"+
		"\678\7#\2\289\5\b\5\29:\7$\2\2:\u008c\3\2\2\2;<\7(\2\2<=\7&\2\2=>\5\4"+
		"\3\2>?\7\'\2\2?@\5\4\3\2@\u008c\3\2\2\2AB\7(\2\2BC\7&\2\2CD\5\4\3\2DE"+
		"\7\'\2\2EF\5\4\3\2FG\7)\2\2GH\5\4\3\2H\u008c\3\2\2\2IJ\7*\2\2JK\7&\2\2"+
		"KL\7@\2\2LM\7+\2\2MN\5\4\3\2NO\7\'\2\2OP\5\4\3\2P\u008c\3\2\2\2QR\7,\2"+
		"\2RS\7&\2\2ST\5\4\3\2TU\7\'\2\2UV\5\4\3\2V\u008c\3\2\2\2W\u008c\7/\2\2"+
		"X\u008c\7\60\2\2YZ\7&\2\2Z[\5\4\3\2[\\\7\'\2\2\\\u008c\3\2\2\2]\u008c"+
		"\7@\2\2^\u008c\7?\2\2_\u008c\7;\2\2`\u008c\7<\2\2a\u008c\7=\2\2b\u008c"+
		"\7>\2\2c\u008c\7\61\2\2d\u008c\7\62\2\2e\u008c\7\63\2\2f\u008c\7\64\2"+
		"\2g\u008c\7\65\2\2h\u008c\7\66\2\2ij\7&\2\2jk\5\4\3\2kl\7\'\2\2lm\7\'"+
		"\2\2m\u008c\3\2\2\2no\7(\2\2op\7&\2\2pq\5\4\3\2qr\7\'\2\2rs\7\'\2\2st"+
		"\5\4\3\2t\u008c\3\2\2\2uv\7#\2\2vw\5\b\5\2wx\7$\2\2xy\7$\2\2y\u008c\3"+
		"\2\2\2z{\7,\2\2{|\7&\2\2|}\5\4\3\2}~\7\'\2\2~\177\7\'\2\2\177\u0080\5"+
		"\4\3\2\u0080\u008c\3\2\2\2\u0081\u0082\7*\2\2\u0082\u0083\7&\2\2\u0083"+
		"\u0084\7@\2\2\u0084\u0085\7+\2\2\u0085\u0086\5\4\3\2\u0086\u0087\7\'\2"+
		"\2\u0087\u0088\7\'\2\2\u0088\u0089\5\4\3\2\u0089\u008c\3\2\2\2\u008a\u008c"+
		"\5\6\4\2\u008b\35\3\2\2\2\u008b \3\2\2\2\u008b\"\3\2\2\2\u008b$\3\2\2"+
		"\2\u008b+\3\2\2\2\u008b-\3\2\2\2\u008b/\3\2\2\2\u008b\67\3\2\2\2\u008b"+
		";\3\2\2\2\u008bA\3\2\2\2\u008bI\3\2\2\2\u008bQ\3\2\2\2\u008bW\3\2\2\2"+
		"\u008bX\3\2\2\2\u008bY\3\2\2\2\u008b]\3\2\2\2\u008b^\3\2\2\2\u008b_\3"+
		"\2\2\2\u008b`\3\2\2\2\u008ba\3\2\2\2\u008bb\3\2\2\2\u008bc\3\2\2\2\u008b"+
		"d\3\2\2\2\u008be\3\2\2\2\u008bf\3\2\2\2\u008bg\3\2\2\2\u008bh\3\2\2\2"+
		"\u008bi\3\2\2\2\u008bn\3\2\2\2\u008bu\3\2\2\2\u008bz\3\2\2\2\u008b\u0081"+
		"\3\2\2\2\u008b\u008a\3\2\2\2\u008c\u00dc\3\2\2\2\u008d\u008e\f\65\2\2"+
		"\u008e\u008f\t\4\2\2\u008f\u00db\5\4\3\66\u0090\u0091\f\64\2\2\u0091\u0092"+
		"\t\5\2\2\u0092\u00db\5\4\3\65\u0093\u0094\f\63\2\2\u0094\u0095\7\13\2"+
		"\2\u0095\u00db\5\4\3\64\u0096\u0097\f\61\2\2\u0097\u0098\7\16\2\2\u0098"+
		"\u00db\5\4\3\62\u0099\u009a\f\60\2\2\u009a\u009b\7A\2\2\u009b\u00db\5"+
		"\4\3\61\u009c\u009d\f/\2\2\u009d\u009e\t\6\2\2\u009e\u00db\5\4\3\60\u009f"+
		"\u00a0\f.\2\2\u00a0\u00a1\t\3\2\2\u00a1\u00db\5\4\3/\u00a2\u00a3\f-\2"+
		"\2\u00a3\u00a4\t\7\2\2\u00a4\u00db\5\4\3.\u00a5\u00a6\f+\2\2\u00a6\u00a7"+
		"\t\b\2\2\u00a7\u00db\5\4\3,\u00a8\u00a9\f*\2\2\u00a9\u00aa\t\t\2\2\u00aa"+
		"\u00db\5\4\3+\u00ab\u00ac\f(\2\2\u00ac\u00ad\7\34\2\2\u00ad\u00db\5\4"+
		"\3)\u00ae\u00af\f\'\2\2\u00af\u00b0\t\n\2\2\u00b0\u00db\5\4\3(\u00b1\u00b2"+
		"\f\5\2\2\u00b2\u00b3\7\67\2\2\u00b3\u00db\5\4\3\6\u00b4\u00b5\f\4\2\2"+
		"\u00b5\u00b6\78\2\2\u00b6\u00db\5\4\3\5\u00b7\u00b8\f\67\2\2\u00b8\u00b9"+
		"\7\4\2\2\u00b9\u00ba\5\16\b\2\u00ba\u00bb\7\5\2\2\u00bb\u00bc\7\5\2\2"+
		"\u00bc\u00db\3\2\2\2\u00bd\u00be\f\66\2\2\u00be\u00bf\7\6\2\2\u00bf\u00c0"+
		"\5\16\b\2\u00c0\u00c1\7\5\2\2\u00c1\u00db\3\2\2\2\u00c2\u00c3\f$\2\2\u00c3"+
		"\u00c4\7&\2\2\u00c4\u00c5\5\16\b\2\u00c5\u00c6\7\'\2\2\u00c6\u00db\3\2"+
		"\2\2\u00c7\u00c8\f\r\2\2\u00c8\u00c9\7&\2\2\u00c9\u00ca\5\16\b\2\u00ca"+
		"\u00cb\7\'\2\2\u00cb\u00cc\7\'\2\2\u00cc\u00db\3\2\2\2\u00cd\u00ce\f\n"+
		"\2\2\u00ce\u00cf\7\4\2\2\u00cf\u00d0\5\16\b\2\u00d0\u00d1\7\5\2\2\u00d1"+
		"\u00d2\7\5\2\2\u00d2\u00d3\7\5\2\2\u00d3\u00db\3\2\2\2\u00d4\u00d5\f\t"+
		"\2\2\u00d5\u00d6\7\6\2\2\u00d6\u00d7\5\16\b\2\u00d7\u00d8\7\5\2\2\u00d8"+
		"\u00d9\7\5\2\2\u00d9\u00db\3\2\2\2\u00da\u008d\3\2\2\2\u00da\u0090\3\2"+
		"\2\2\u00da\u0093\3\2\2\2\u00da\u0096\3\2\2\2\u00da\u0099\3\2\2\2\u00da"+
		"\u009c\3\2\2\2\u00da\u009f\3\2\2\2\u00da\u00a2\3\2\2\2\u00da\u00a5\3\2"+
		"\2\2\u00da\u00a8\3\2\2\2\u00da\u00ab\3\2\2\2\u00da\u00ae\3\2\2\2\u00da"+
		"\u00b1\3\2\2\2\u00da\u00b4\3\2\2\2\u00da\u00b7\3\2\2\2\u00da\u00bd\3\2"+
		"\2\2\u00da\u00c2\3\2\2\2\u00da\u00c7\3\2\2\2\u00da\u00cd\3\2\2\2\u00da"+
		"\u00d4\3\2\2\2\u00db\u00de\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2"+
		"\2\2\u00dd\5\3\2\2\2\u00de\u00dc\3\2\2\2\u00df\u00e1\7D\2\2\u00e0\u00df"+
		"\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e0\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3"+
		"\7\3\2\2\2\u00e4\u00eb\5\4\3\2\u00e5\u00e7\t\13\2\2\u00e6\u00e8\5\4\3"+
		"\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00ea\3\2\2\2\u00e9\u00e5"+
		"\3\2\2\2\u00ea\u00ed\3\2\2\2\u00eb\u00e9\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec"+
		"\u00f0\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ee\u00f0\3\2\2\2\u00ef\u00e4\3\2"+
		"\2\2\u00ef\u00ee\3\2\2\2\u00f0\t\3\2\2\2\u00f1\u00f6\5\f\7\2\u00f2\u00f3"+
		"\79\2\2\u00f3\u00f5\5\f\7\2\u00f4\u00f2\3\2\2\2\u00f5\u00f8\3\2\2\2\u00f6"+
		"\u00f4\3\2\2\2\u00f6\u00f7\3\2\2\2\u00f7\13\3\2\2\2\u00f8\u00f6\3\2\2"+
		"\2\u00f9\u00ff\7@\2\2\u00fa\u00fb\7@\2\2\u00fb\u00fc\7\37\2\2\u00fc\u00ff"+
		"\5\4\3\2\u00fd\u00ff\7:\2\2\u00fe\u00f9\3\2\2\2\u00fe\u00fa\3\2\2\2\u00fe"+
		"\u00fd\3\2\2\2\u00ff\r\3\2\2\2\u0100\u0105\5\20\t\2\u0101\u0102\79\2\2"+
		"\u0102\u0104\5\20\t\2\u0103\u0101\3\2\2\2\u0104\u0107\3\2\2\2\u0105\u0103"+
		"\3\2\2\2\u0105\u0106\3\2\2\2\u0106\17\3\2\2\2\u0107\u0105\3\2\2\2\u0108"+
		"\u011b\5\4\3\2\u0109\u010a\7@\2\2\u010a\u011b\7\37\2\2\u010b\u010c\7@"+
		"\2\2\u010c\u010d\7\37\2\2\u010d\u011b\5\4\3\2\u010e\u010f\7?\2\2\u010f"+
		"\u011b\7\37\2\2\u0110\u0111\7?\2\2\u0111\u0112\7\37\2\2\u0112\u011b\5"+
		"\4\3\2\u0113\u0114\7\61\2\2\u0114\u011b\7\37\2\2\u0115\u0116\7\61\2\2"+
		"\u0116\u0117\7\37\2\2\u0117\u011b\5\4\3\2\u0118\u011b\7:\2\2\u0119\u011b"+
		"\3\2\2\2\u011a\u0108\3\2\2\2\u011a\u0109\3\2\2\2\u011a\u010b\3\2\2\2\u011a"+
		"\u010e\3\2\2\2\u011a\u0110\3\2\2\2\u011a\u0113\3\2\2\2\u011a\u0115\3\2"+
		"\2\2\u011a\u0118\3\2\2\2\u011a\u0119\3\2\2\2\u011b\21\3\2\2\2\21\26\30"+
		"\'\62\u008b\u00da\u00dc\u00e2\u00e7\u00eb\u00ef\u00f6\u00fe\u0105\u011a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}